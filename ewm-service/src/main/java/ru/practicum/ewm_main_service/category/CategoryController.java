package ru.practicum.ewm_main_service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.category.dto.NewCategoryDto;
import ru.practicum.ewm_main_service.category.service.CategoryService;
import ru.practicum.ewm_main_service.exception.ValidationException;
import ru.practicum.ewm_main_service.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addNewCategory(@RequestBody NewCategoryDto dto) {
        return categoryService.addNewCategory(dto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Получен запрос на получение информации о категориях");
        if ((from < 0) || (size < 1)) {
            throw new ValidationException("Неверные параметры запроса");
        }
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") long catId) {
        log.info("Получен запрос на получение информации о категории с ID={}", catId);
        return categoryService.getCategoryById(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable(name = "catId") long catId,
                                      @RequestBody CategoryDto dto) {
        return categoryService.updateCategory(catId, dto);
    }
}