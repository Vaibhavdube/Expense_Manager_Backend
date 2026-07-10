package in.vaibhav.moneymanager.controller;

import in.vaibhav.moneymanager.dto.CategoryDTO;
import in.vaibhav.moneymanager.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory =
                categoryService.saveCategory(categoryDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCategory);
    }

    @GetMapping
    //uper request mapping k url sa chlega ya method bus
    public ResponseEntity<List<CategoryDTO>> getCategoriesForCurrentUser() {

        List<CategoryDTO> categories= categoryService.getCategoriesForCurrentUser();
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/{type}")

    //{type} ka matlab hai:
    //
    //"Yahan koi bhi value aa sakti hai"
    public ResponseEntity<List<CategoryDTO>>
    getCategoriesByTypeForCurrentUser(@PathVariable String type)
    {
        List<CategoryDTO> categories =
                categoryService
                        .getCategoriesByTypeForCurrentUser(type);

        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO)
    {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);

        return ResponseEntity.ok(updatedCategory);
    }

}
