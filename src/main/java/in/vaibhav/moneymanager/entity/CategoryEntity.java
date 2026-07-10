package in.vaibhav.moneymanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


      @Column(updatable = false)
      @CreationTimestamp
      private LocalDateTime createdAt;

      @UpdateTimestamp
      private LocalDateTime updatedAt;

      private String type;

      private String icon;

      //Yahan many = categories, aur one = profile/user
      @ManyToOne(fetch=FetchType.LAZY)
      @JoinColumn(name="profile_id", nullable = false)
      //Category table mein profile_id naam ka foreign key column banao
      //nullable = false ka matlab:
      //
      //Category bina user ke exist nahi kar sakti.
      private ProfileEntity profile;
}
