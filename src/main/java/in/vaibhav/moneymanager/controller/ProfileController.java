package in.vaibhav.moneymanager.controller;

import in.vaibhav.moneymanager.dto.AuthDTO;
import in.vaibhav.moneymanager.dto.ProfileDTO;
import in.vaibhav.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<?> registerProfile(
            @RequestBody ProfileDTO profileDTO) {

        try {

            ProfileDTO registered =
                    profileService.registerProfile(profileDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            Map.of(
                                    "message",
                                    "Registration successful. Please verify your email.",
                                    "user",
                                    registered
                            )
                    );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(
            @RequestParam String token) {

        boolean activated =
                profileService.activateProfile(token);

        if (activated) {

            return ResponseEntity.ok(
                    "Email verified successfully. Please login."
            );

        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Invalid or expired activation link.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthDTO authDTO) {

        try {

            if (!profileService.isAccountActive(
                    authDTO.getEmail())) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                                Map.of(
                                        "message",
                                        "Please verify your email first."
                                )
                        );
            }

            Map<String, Object> response =
                    profileService.authenticateAndGenerateToken(authDTO);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/test")
    public String test() {
        return "test success";
    }
}


