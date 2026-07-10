package in.vaibhav.moneymanager.repository;

import in.vaibhav.moneymanager.entity.ProfileEntity;
import in.vaibhav.moneymanager.service.ProfileService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long>
        {

    Optional<ProfileEntity>findByEmail(String email);
    Optional<ProfileEntity>findByActivationToken(String activationToken);
}
