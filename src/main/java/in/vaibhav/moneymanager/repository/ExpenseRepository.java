package in.vaibhav.moneymanager.repository;

import in.vaibhav.moneymanager.entity.ExpenseEntity;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    //yaha spring khud methodh padhker internally sql bna lega
    //yaha parameter m voh he pass hoga jo sql mein chahiya
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc( Long profileId);

   //jpql used we write own query no spring will make because this query is so long
    //jab query lambi ho toh jpql leta h
    //yaha: ka baad jo hai voh @param mein jaroori hai jpql syntax h
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id=:profileId")
    //ya add ho raha hai isliya BigDecimal

    BigDecimal findTotalExpenseByProfileId(@Param("profileId")Long profileId);

    //SELECT * FROM expense WHERE profile_id=? AND date BETWEEN ? AND ?AND LOWER(name)LIKE LOWER('%keyword%')
    List<ExpenseEntity>findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate,LocalDate endDate, String keyword, Sort sort);

    List<ExpenseEntity>findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);
}