package ru.practicum.ewm_main_service.category.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Название категории не может быть пустым")
    @Length(min = 1, max = 50)
    private String name;
}
