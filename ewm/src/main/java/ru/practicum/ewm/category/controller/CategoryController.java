package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getAllCats(@RequestParam(name = "from") int from,
                                        @RequestParam(name = "size") int size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCatById(@PathVariable long catId) {
        return categoryService.getCategoryById(catId);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryDto categoryDto) {
        return categoryService.updateCategory(ownerId, categoryDto);
    }

    @PostMapping("/admin/categories")
    public CategoryDto addCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryRequestDto categoryRequestDto) {
        return categoryService.addCategory(ownerId, categoryRequestDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<String> deleteCatById(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long catId) {
         categoryService.deleteCategory(ownerId, catId);
         return ResponseEntity.ok("Категория удалена");
    }
}
