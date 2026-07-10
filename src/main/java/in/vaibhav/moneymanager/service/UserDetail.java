package in.vaibhav.moneymanager.service;

import in.vaibhav.moneymanager.entity.ProfileEntity;
import in.vaibhav.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserDetail implements UserDetailsService {

/* Controller
↓
    AuthenticationManager.authenticate()
        ↓
    UserDetailsService
↓
    loadUserByUsername()    */

    private final ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        ProfileEntity existingProfile = profileRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException( "User not found with email: " + email)
                        );
//WHY THIS?
//
//Because Spring Security wants:
//
//UserDetails object
//
//NOT your entity directly.

        //Spring Wants Standard Security Format
        //username
        //password
        //roles
        //authorities
        return User
                .builder()
                .username(existingProfile.getEmail())
                .password(existingProfile.getPassword())
                .roles("USER")
                .build();
    }
}
