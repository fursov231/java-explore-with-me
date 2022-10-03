package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.service.CategoryServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    @PatchMapping
    public CategoryDto updateCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryDto categoryDto) {
        return categoryServiceImpl.updateCategory(ownerId, categoryDto);
    }

    @PostMapping
    public CategoryDto addCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryRequestDto categoryRequestDto) {
        return categoryServiceImpl.addCategory(ownerId, categoryRequestDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<String> deleteCatById(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long catId) {
        categoryServiceImpl.deleteCategory(ownerId, catId);
        return ResponseEntity.ok("Категория удалена");
    }
}
