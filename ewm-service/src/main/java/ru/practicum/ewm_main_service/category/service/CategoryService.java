package ru.practicum.ewm_main_service.category.service;

import ru.practicum.ewm_main_service.category.Category;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(NewCategoryDto dto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(long catId);

    CategoryDto updateCategory(long catId, CategoryDto dto);

    void deleteCategoryById(Long catId);

    Category get(Long category);
}
