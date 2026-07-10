package in.vaibhav.moneymanager.service;

import in.vaibhav.moneymanager.dto.ExpenseDTO;
import in.vaibhav.moneymanager.entity.CategoryEntity;
import in.vaibhav.moneymanager.entity.ExpenseEntity;
import in.vaibhav.moneymanager.entity.ProfileEntity;
import in.vaibhav.moneymanager.repository.CategoryRepository;
import in.vaibhav.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {

        ProfileEntity profile =
                profileService.getCurrentProfile();

        CategoryEntity category =
                categoryRepository
                        .findByIdAndProfileId(
                                expenseDTO.getCategoryId(),
                                profile.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"));
        ExpenseEntity newExpense = toEntity(expenseDTO, profile, category);
        newExpense = expenseRepository.save(newExpense);

        return toDTO(newExpense);

    }








    //filter expenses
    public List<ExpenseDTO> filterExpenses (LocalDate startDate, LocalDate endDate,String keyword, Sort sort)
    {
        ProfileEntity profile =profileService.getCurrentProfile();
       List<ExpenseEntity> list= expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
       return list.stream().map(this::toDTO).toList();
    }

    //Notification
    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date)
    {
      List<ExpenseEntity> list  =expenseRepository.findByProfileIdAndDate( profileId,date);
      return list.stream().map(this::toDTO).toList();
    }


    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(category)
                .profile(profile)
                .build();
    }


    //Retrieves all expenses for current month/based on the start date and end date

//SELECT *

//FROM expenses

//WHERE profile_id = 10

//AND date BETWEEN '2026-06-01' AND '2026-06-30';


    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {


        ProfileEntity profile = profileService.getCurrentProfile();

        LocalDate now = LocalDate.now();

        LocalDate startDate = now.withDayOfMonth(1);

        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);

        return list.stream().map(this::toDTO).toList();


    }




    private ExpenseDTO toDTO(ExpenseEntity expense) {

        return ExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .icon(expense.getIcon())
                .date(expense.getDate())
                .amount(expense.getAmount())
                .categoryId(expense.getCategory() != null ? expense.getCategory().getId() : null)
                .categoryName(expense.getCategory() != null ? expense.getCategory().getName() : "N/A")
                .profileId(expense.getProfile().getId())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }


    public void deleteExpense(Long expenseId) {

        ProfileEntity profile = profileService.getCurrentProfile();

        ExpenseEntity entity = expenseRepository
                .findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!entity.getProfile().getId().equals(profile.getId())) {

            throw new RuntimeException("Unauthorized to delete this expense");


        }

        expenseRepository.delete(entity);
    }




    // Get latest 5 expenses for current user
    public List<ExpenseDTO> getLatest5ExpenseForCurrentUser() {

        ProfileEntity profile = profileService.getCurrentProfile();

        List<ExpenseEntity> list =
                expenseRepository.findTop5ByProfileIdOrderByDateDesc(
                        profile.getId());

        return list.stream()
                .map(this::toDTO)
                .toList();
    }

    // Get total expenses for current user
    public BigDecimal getTotalExpenseForCurrentUser() {

        ProfileEntity profile =
                profileService.getCurrentProfile();

        BigDecimal total =
                expenseRepository.findTotalExpenseByProfileId(
                        profile.getId());

        return total != null ? total : BigDecimal.ZERO;
    }
}

