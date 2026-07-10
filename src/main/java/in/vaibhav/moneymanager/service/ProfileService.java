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
        //dto--->entity conversion
        ProfileEntity newProfile = toEntity(profileDTO);
        //random token genreration
        newProfile.setActivationToken(UUID.randomUUID().toString());
        //jo entity banayi usko DB mein save kerdiya save in DB
        //before save to DB be encode password through passwordEncoder.encode()
        ;
        newProfile = profileRepository.save(newProfile);
//DB mein entity save hona ka baad mtlb sign up check hona ka baad generate activation link
        //Activation email
        String activationLink =activationURL+
                "/api/v2.0/activate?token="
                        + newProfile.getActivationToken();

        String subject = "Tu nhi toh kon bey";

        String body =
                "Click below to activate your account:\n\n"
                        + activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject, body);
        //aab client ko entity nhi DTO bhej
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
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }

    //kya user kaaccount deactivate toh nhi
    public Boolean isAccountActive(String email) {

        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
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




