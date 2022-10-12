package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(long catId);

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);
}
