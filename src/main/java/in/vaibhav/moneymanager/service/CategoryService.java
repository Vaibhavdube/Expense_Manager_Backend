package in.vaibhav.moneymanager.service;

import in.vaibhav.moneymanager.dto.CategoryDTO;
import in.vaibhav.moneymanager.entity.CategoryEntity;
import in.vaibhav.moneymanager.entity.ProfileEntity;
import in.vaibhav.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;


    //Get Logged-in Profile
    //        ↓
    //Duplicate Name Check
    //        ↓
    //DTO → Entity
    //        ↓
    //Save
    //        ↓
    //Entity → DTO
    //        ↓
    //Return
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();

        boolean categoryExists = categoryRepository
                .existsByNameAndProfileId(
                        categoryDTO.getName(),
                        profile.getId()
                );

        if (categoryExists) {
            throw new RuntimeException("Category already exists");
        }

        CategoryEntity newCategory = toEntity(categoryDTO, profile);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);


    }

//getcurrent user category
    public List<CategoryDTO> getCategoriesForCurrentUser()
    {
        ProfileEntity profile =
                profileService.getCurrentProfile();

        return categoryRepository
                .findByProfileId(profile.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }


    //Logged-in User
    //      ↓
    //Category id exist?
    
    //      ↓
    //Update fields
    //      ↓
    //Save
    //      ↓
    //Return DTO
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO)
    {
        ProfileEntity profile = profileService.getCurrentProfile();

        CategoryEntity category = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                ));

        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setIcon(categoryDTO.getIcon());

        CategoryEntity updatedCategory = categoryRepository.save(category);

        return toDTO(updatedCategory);
    }

    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type)
    {
        ProfileEntity profile =
                profileService.getCurrentProfile();

        return categoryRepository
                .findByTypeAndProfileId(
                        type,
                        profile.getId()
                )
                .stream()
                .map(this::toDTO)
                .toList();
    }


    //helper method
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile)
    {
       return CategoryEntity.builder()
               .name(categoryDTO.getName())
               .icon(categoryDTO.getIcon())
               .profile(profile)
               .type(categoryDTO.getType())
               .build();
    }

    private CategoryDTO toDTO(CategoryEntity entity)
    {
        return CategoryDTO.builder()
                .id(entity.getId())
                .profileId(entity.getProfile()!=null? entity.getProfile().getId(): null)
                .name(entity.getName())
                .icon(entity.getIcon())
                .createdAt((entity.getCreatedAt()))
                        .updatedAt(entity.getUpdatedAt())
                        .type(entity.getType())
                         .build();

    }

}
