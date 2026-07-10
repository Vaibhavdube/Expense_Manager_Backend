package in.vaibhav.moneymanager.repository;

import in.vaibhav.moneymanager.entity.ExpenseEntity;
import in.vaibhav.moneymanager.entity.IncomeEntity;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

    //Ye derived query method hai.
    //Spring Data JPA method name ko parse karke
    //automatically query generate karta hai.
    //yaha spring khud methodh padhker internally sql bna lega
    //yaha parameter m voh he pass hoga jo sql mein chahiya
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc( Long profileId);

    //jpql used we write own query no spring will make because this query is so long
    //jab query lambi ho toh jpql leta h
    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.profile.id=:profileId")
    BigDecimal findTotalIncomeByProfileId(@Param("profileId")Long profileId);

    //SELECT * FROM expense WHERE profile_id=? AND date BETWEEN ? AND ?AND LOWER(name)LIKE LOWER('%keyword%')
    List<IncomeEntity>findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

    List<IncomeEntity>findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}