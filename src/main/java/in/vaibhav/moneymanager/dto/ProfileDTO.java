package in.vaibhav.moneymanager.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private Long id;

    private String fullName;
    private String email;
    private String password;
    private String profileImageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
