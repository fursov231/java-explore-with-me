package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.client.CategoryAdminClient;
import ru.practicum.ewm.category.client.CategoryPublicClient;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryPublicClient publicClient;
    private final CategoryAdminClient adminClient;

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCats(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicClient.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCatById(@PathVariable long catId) {
        return publicClient.getCategoryById(catId);
    }

    @PatchMapping("/admin/categories")
    public ResponseEntity<Object> updateCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryDto categoryDto) {
        return adminClient.updateCategory(ownerId, categoryDto);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<Object> addCat(@RequestHeader("X-Sharer-User-Id") long ownerId, CategoryRequestDto categoryRequestDto) {
        return adminClient.addCategory(ownerId, categoryRequestDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<Object> deleteCatById(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long catId) {
        return adminClient.deleteCategory(ownerId, catId);
    }
}
