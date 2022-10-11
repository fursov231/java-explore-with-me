package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryAdminController {
    private final CategoryService categoryService;

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
