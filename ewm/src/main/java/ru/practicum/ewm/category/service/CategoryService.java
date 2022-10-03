package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(long catId);

    CategoryDto updateCategory(long ownerId, CategoryDto categoryDto);

    CategoryDto addCategory(long ownerId, CategoryRequestDto categoryRequestDto);

    void deleteCategory(long ownerId, long catId);
}
