package ru.practicum.ewm_main_service.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;
import ru.practicum.ewm_main_service.category.Category;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.category.dto.CategoryMapper;
import ru.practicum.ewm_main_service.category.dto.NewCategoryDto;
import ru.practicum.ewm_main_service.category.repository.CategoryRepository;
import ru.practicum.ewm_main_service.exception.DataAlreadyExists;
import ru.practicum.ewm_main_service.exception.ValidationException;

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
        return CategoryMapper.toCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = repository.findById(catId).orElseThrow(() -> new DataNotFoundException("Категория с таким id не найдена."));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto dto) {
        if (dto == null) {
            throw new ValidationException("Не переданы данные категории. Ошибка валидации.");
        }
        if (dto.getId() == null) {
            dto.setId(catId);
        }
        Category categoryUpdate = repository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Категория с ID=" + catId + " не найдена!"));
        if (dto.getName() != null) {
            Category categoryByName = repository.findCategoryByName(dto.getName());
            if (categoryByName == null) {
                categoryUpdate.setName(dto.getName());
            } else if (!categoryByName.getId().equals(catId)) {
                throw new DataAlreadyExists("Категория с таким названием уже существует.");
            }
        }
        return CategoryMapper.toCategoryDto(repository.save(categoryUpdate));
    }

    @Override
    public Category get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format("Категория не найдена id = %s", id)));
    }

    @Override
    public void deleteCategoryById(Long catId) {

    }


}
