package ru.practicum.ewm_main_service.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.category.Category;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.category.dto.CategoryMapper;
import ru.practicum.ewm_main_service.category.dto.NewCategoryDto;
import ru.practicum.ewm_main_service.category.repository.CategoryRepository;
import ru.practicum.ewm_main_service.exception.DataAlreadyExists;
import ru.practicum.ewm_main_service.exception.ValidationException;
import ru.practicum.ewm_main_service.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    @Override
    public CategoryDto addNewCategory(NewCategoryDto dto) {
        if (dto.getName() == null) {
            throw new ValidationException("Field: name. Error: must not be blank. Value: null.");
        }
        if (repository.findCategoryByName(dto.getName()) != null) {
            throw new DataAlreadyExists("could not execute statement");
        }
        Category category = repository.save(new Category(null, dto.getName()));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        List<Category> categories = repository.findAll(PageRequest.of(from, size, sortById)).getContent();
        log.info("Number of categories: {}", categories.size());
        return CategoryMapper.toCategoryDtoList(categories);
    }
}
