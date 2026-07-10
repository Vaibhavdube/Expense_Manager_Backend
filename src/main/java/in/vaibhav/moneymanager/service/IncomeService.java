package in.vaibhav.moneymanager.service;

import in.vaibhav.moneymanager.dto.ExpenseDTO;
import in.vaibhav.moneymanager.dto.IncomeDTO;
import in.vaibhav.moneymanager.entity.CategoryEntity;
import in.vaibhav.moneymanager.entity.ExpenseEntity;
import in.vaibhav.moneymanager.entity.IncomeEntity;
import in.vaibhav.moneymanager.entity.ProfileEntity;
import in.vaibhav.moneymanager.repository.CategoryRepository;
import in.vaibhav.moneymanager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    //add
    public IncomeDTO addIncome(IncomeDTO incomeDTO) {

        ProfileEntity profile =
                profileService.getCurrentProfile();

        CategoryEntity category =
                categoryRepository
                        .findByIdAndProfileId(
                                incomeDTO.getCategoryId(),
                                profile.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"));

        IncomeEntity newIncome = toEntity(incomeDTO, profile, category);
        newIncome =  incomeRepository.save(newIncome);

        return toDTO(newIncome);

    }


//toentity
    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category)
    {
        return IncomeEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(category)
                .profile(profile)
                .build();
    }








//todto
    private IncomeDTO toDTO(IncomeEntity income) {

        return IncomeDTO.builder()
                .id(income.getId())
                .name(income.getName())
                .icon(income.getIcon())
                .date(income.getDate())
                .amount(income.getAmount())
                .categoryId(income.getCategory()!=null? income.getCategory().getId():null)
                .categoryName(income.getCategory()!=null? income.getCategory().getName(): "N/A")
                .profileId(income.getProfile().getId())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .build();
    }



//currentmonth
    public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser() {


        ProfileEntity profile = profileService.getCurrentProfile();

        LocalDate now = LocalDate.now();

        LocalDate startDate = now.withDayOfMonth(1);

        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);

        return list.stream().map(this::toDTO).toList();


    }
    //filter income
    public List<IncomeDTO> filterIncome (LocalDate startDate, LocalDate endDate,String keyword, Sort sort)
    {
        ProfileEntity profile =profileService.getCurrentProfile();
        List<IncomeEntity> list= incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
        return list.stream().map(this::toDTO).toList();
    }


    public  void deleteIncome(Long incomeId) {

        ProfileEntity profile = profileService.getCurrentProfile();

        IncomeEntity entity = incomeRepository
                .findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!entity.getProfile().getId().equals(profile.getId())) {

            throw new RuntimeException("Unauthorized to delete this income");

        }

        incomeRepository.delete(entity);
    }






    // Get latest 5 expenses for current user
    public List<IncomeDTO> getLatest5IncomeForCurrentUser() {

        ProfileEntity profile = profileService.getCurrentProfile();

        List<IncomeEntity> list =
                incomeRepository.findTop5ByProfileIdOrderByDateDesc(
                        profile.getId());

        return list.stream()
                .map(this::toDTO)
                .toList();
    }

    // Get total expenses for current user
    public BigDecimal getTotalIncomeForCurrentUser() {

        ProfileEntity profile =
                profileService.getCurrentProfile();

        BigDecimal total =
                incomeRepository.findTotalIncomeByProfileId(
                        profile.getId());

        return total != null ? total : BigDecimal.ZERO;
    }
}
