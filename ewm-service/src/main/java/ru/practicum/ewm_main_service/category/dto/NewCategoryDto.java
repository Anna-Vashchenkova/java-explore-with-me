package ru.practicum.ewm_main_service.category.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewCategoryDto {
    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 50)
    private String name;
}
