package in.vaibhav.moneymanager.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private String profileImageUrl;
    private String activationToken;
    //isActive abhi false hai jaise he user signup ya login kerega verification link sa ya true ho jaayega
    private Boolean isActive;    //wrapper Boolen- true, false, null
                                  //boolean-true ya false
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @PrePersist
     public void prePersist()
     {
         if(this.isActive==null)   //null mtlb no true, no false, no empty
         {
             isActive=false;
         }
     }






}
