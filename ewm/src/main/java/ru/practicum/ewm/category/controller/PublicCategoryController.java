package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping
    public List<CategoryDto> getAllCats(@RequestParam(name = "from") int from,
                                        @RequestParam(name = "size") int size) {
        return categoryServiceImpl.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCatById(@PathVariable long catId) {
        return categoryServiceImpl.getCategoryById(catId);
    }
}
