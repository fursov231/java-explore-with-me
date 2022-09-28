package ru.practicum.ewm.category.util;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.model.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static Category requestDtoToCategory(CategoryRequestDto categoryRequestDto) {
        return Category.builder()
                .name(categoryRequestDto.getName())
                .build();
    }

}
