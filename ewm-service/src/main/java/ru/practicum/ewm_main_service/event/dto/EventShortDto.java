package ru.practicum.ewm_main_service.event.dto;

import lombok.*;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventShortDto {
    private Long id;
    @NotBlank(message = "Поле название не может быть пустым.")
    private String title;
    @NotBlank(message = "Поле краткое описание не может быть пустым.")
    private String annotation;
    @NotNull(message = "Поле категория события не может быть пустым.")
    private CategoryDto category;
    @NotBlank(message = "Дата проведения события не может быть пустой.")
    private String eventDate; //yyyy-MM-dd HH:mm:ss
    @NotNull(message = "Поле инициатор не может быть пустым.")
    private UserShortDto initiator;
    @NotNull(message = "Поле оплата не может быть пустым.")
    private boolean paid;
    private Long confirmedRequests;
    private long views;
}
