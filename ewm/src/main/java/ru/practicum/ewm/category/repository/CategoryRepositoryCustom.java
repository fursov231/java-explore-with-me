package ru.practicum.ewm.category.repository;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;

public interface CategoryRepositoryCustom {
    Category update(long ownerId, CategoryDto categoryDto);
}
