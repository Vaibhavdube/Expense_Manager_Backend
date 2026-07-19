package in.vaibhav.moneymanager.service;
import in.vaibhav.moneymanager.util.JwtUtil;

import in.vaibhav.moneymanager.dto.AuthDTO;
import in.vaibhav.moneymanager.dto.ProfileDTO;
import in.vaibhav.moneymanager.entity.ProfileEntity;
import in.vaibhav.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;

    private final JwtUtil jwtUtil;

    @Value("${app.activation.url}")
    private String activationURL;
    private final PasswordEncoder passwordEncoder;

    //ek method banaya type ProfileDto hai issa ProfileDTo ka object milega
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {

        if (profileRepository.findByEmail(profileDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email.");
        }

        ProfileEntity newProfile = toEntity(profileDTO);

        newProfile.setActivationToken(UUID.randomUUID().toString());

        newProfile = profileRepository.save(newProfile);

        String activationLink =
                activationURL + "/api/v2.0/activate?token="
                        + newProfile.getActivationToken();

        String subject = "Verify your Email";

        String body = """
            Welcome to Expense Manager.

            Please click the link below to verify your email.

            %s

            If you did not register, ignore this email.
            """.formatted(activationLink);

        emailService.sendEmail(
                newProfile.getEmail(),
                subject,
                body
        );

        return toDTO(newProfile);
    }

    //aab DTO ka object hiber samajh nhi paayega isliya entitymein convert
    //DTO → Entity
    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName((profileDTO.getFullName()))
                .email(profileDTO.getEmail())
                .password((passwordEncoder.encode(profileDTO.getPassword())))
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }

    //entity to DTo for  to send client
    public ProfileDTO toDTO(ProfileEntity profileEntity) {
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {

        return profileRepository
                .findByActivationToken(activationToken)
                .map(profile -> {

                    profile.setIsActive(true);

                    profile.setActivationToken(null);

                    profileRepository.save(profile);

                    return true;

                }).orElse(false);
    }

    //kya user kaaccount deactivate toh nhi
    public Boolean isAccountActive(String email) {

        ProfileEntity profile =
                profileRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        return profile.getIsActive();
    }

    /// / JWT authenticated request ke baad  (jwt filter jab use hoga)
    /// / currently logged-in user ka full DB profile fetch karta hai
    /// ////ya tab use hoga jab tujha password change update ya kuch bhi kerna hoga tab kaam aayega ya method
    public ProfileEntity getCurrentProfile() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Profile not found" + authentication.getName())
                );
    }

    //is method ke through tu koi bhi public visible data dekh sakta hai password token wagera chodhker sirf DTO portion
    //Requirment from frontend
    //name
    //profileImage
    //bio
    //->
    //Controller
    //↓
    //getPublicProfile(null)
    //↓
    //getCurrentProfile()
    //↓
    //DTO response
    public ProfileDTO getPublicProfile(String email) {

        ProfileEntity currentUser;
//agar frontend form mein email nhi bheje user na toh null chlega
        if (email == null || email.isBlank()) {

            currentUser = getCurrentProfile();  //agar email nhi toh pura data fetch
//agar user na email bheje toh else chlega
        } else {

            currentUser = profileRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Profile not found"
                            )
                    );
        }

        return ProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .profileImageUrl(
                        currentUser.getProfileImageUrl()
                )
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword())));
//generate jwt token
            String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of(
                    "token", token,
                    "user", getPublicProfile((authDTO.getEmail()))
            );
        } catch (Exception e) {
            throw new RuntimeException(("Invalid email and password"));
        }
    }
}




