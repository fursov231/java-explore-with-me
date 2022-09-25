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

    @GetMapping
    public ResponseEntity<Object> getAllCats(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicClient.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getCatById(@PathVariable long catId) {
        return publicClient.getCategoryById(catId);
    }

    @PatchMapping
    public ResponseEntity<Object> updateCat(@RequestHeader("X-Sharer-User-Id") long userId, CategoryDto categoryDto) {
        return adminClient.updateCategory(userId, categoryDto);
    }

    @PostMapping
    public ResponseEntity<Object> addCat(@RequestHeader("X-Sharer-User-Id") long userId, CategoryRequestDto categoryRequestDto) {
        return adminClient.addCategory(userId, categoryRequestDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteCatById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long catId) {
        return adminClient.deleteCategory(userId, catId);
    }
}
