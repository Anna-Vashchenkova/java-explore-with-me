package ru.practicum.ewm_main_service.category.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewCategoryDto {
    @Column(name = "name", nullable = false)
    @Length(min = 1, max = 50)
    private String name;
}
