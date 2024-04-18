package ru.practicum.ewm_main_service.category.service;

import ru.practicum.ewm_main_service.category.Category;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.category.dto.NewCategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryDto addNewCategory(NewCategoryDto dto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(long catId);

    Optional<Category> getOptionalCategoryById(long catId);

    CategoryDto updateCategory(long catId, CategoryDto dto);

    void deleteCategoryById(Long catId);

    Category get(Long category);
}
