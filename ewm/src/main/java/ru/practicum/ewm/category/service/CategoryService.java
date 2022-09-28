package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.util.CategoryMapper;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<CategoryDto> getAllCategories(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);
        return categoryPage.getContent().stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(long catId) {
        CategoryDto result = null;
        Optional<Category> categoryOptional = categoryRepository.findById(catId);
        if (categoryOptional.isPresent()) {
            result = CategoryMapper.toDto(categoryOptional.get());
        }
        return result;
    }

    @Transactional
    public CategoryDto updateCategory(long ownerId, CategoryDto categoryDto) {
       Optional<User> optionalUser = userRepository.findById(ownerId);
       if (optionalUser.isPresent()) {
           return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
       }
        throw new NotFoundException("Указанный пользователь не найден");
    }

    @Transactional
    public CategoryDto addCategory(long ownerId, CategoryRequestDto categoryRequestDto) {
        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (optionalUser.isPresent()) {
           return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.requestDtoToCategory(categoryRequestDto)));
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }

    @Transactional
    public void deleteCategory(long ownerId, long catId) {
        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (optionalUser.isPresent()) {
            categoryRepository.deleteById(catId);
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }
}
