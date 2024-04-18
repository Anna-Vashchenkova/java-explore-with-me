package ru.practicum.ewm_main_service.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_main_service.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByName(String name);
}
