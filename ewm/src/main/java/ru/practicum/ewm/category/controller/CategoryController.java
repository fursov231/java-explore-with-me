package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getAllCats(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCatById(@PathVariable long catId) {
        return categoryService.getCategoryById(catId);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCat(@RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/admin/categories")
    public CategoryDto addCat(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<String> deleteCatById(@PathVariable long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.ok("Категория удалена");
    }
}
