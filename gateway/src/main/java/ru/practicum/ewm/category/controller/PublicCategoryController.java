package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.category.client.CategoryPublicClient;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {
    private final CategoryPublicClient publicClient;

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCats(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicClient.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCatById(@PathVariable long catId) {
        return publicClient.getCategoryById(catId);
    }
}
