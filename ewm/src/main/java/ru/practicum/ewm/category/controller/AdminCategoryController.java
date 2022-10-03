package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PatchMapping
    public CategoryDto updateCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryDto categoryDto) {
        return categoryService.updateCategory(ownerId, categoryDto);
    }

    @PostMapping
    public CategoryDto addCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryRequestDto categoryRequestDto) {
        return categoryService.addCategory(ownerId, categoryRequestDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<String> deleteCatById(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long catId) {
        categoryService.deleteCategory(ownerId, catId);
        return ResponseEntity.ok("Категория удалена");
    }
}
