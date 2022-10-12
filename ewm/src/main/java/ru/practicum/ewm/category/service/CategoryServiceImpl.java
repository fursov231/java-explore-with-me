package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.util.CategoryMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public List<CategoryDto> getAllCategories(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);
        return categoryPage.getContent().stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(long catId) {
        Optional<Category> optionalCategory = categoryRepository.findById(catId);
        return optionalCategory.map(CategoryMapper::toDto).orElse(null);
    }

    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryDto.getName());
        if (optionalCategory.isPresent()) {
            throw new ValidationException("Категория с указанным именем уже существует");
        }
        log.info("Категория id=${} обновлена", categoryDto.getId());
        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findByName(newCategoryDto.getName());
        if (optionalCategory.isPresent()) {
            throw new ValidationException("Категория с указанным именем уже существует");
        }
        Category category = CategoryMapper.requestDtoToCategory(newCategoryDto);
        Category saved = categoryRepository.save(category);
        log.info("Категория ${} сохранена", category.getName());
        return CategoryMapper.toDto(saved);
    }

    @Transactional
    public void deleteCategory(long catId) {
        List<Event> events = eventRepository.findAllByCategory_Id(catId);
        if (!events.isEmpty()) {
            throw new ValidationException("Удаление категорий, которые связаны с событиями запрещено");
        }
        log.info("Категория id=${} удалена", catId);
        categoryRepository.deleteById(catId);
    }
}
