package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.client.CategoryAdminClient;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {
    private final CategoryAdminClient adminClient;

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
