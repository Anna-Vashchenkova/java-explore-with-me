package ru.practicum.ewm_main_service.event.dto;

import lombok.*;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.event.model.Location;
import ru.practicum.ewm_main_service.user.dto.UserShortDto;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventFullDto {
    private Long id;
    @NotBlank(message = "Поле название не может быть пустым.")
    private String title;
    @NotBlank(message = "Поле краткое описание не может быть пустым.")
    private String annotation;
    private String description;
    @NotNull(message = "Поле категория события не может быть пустым.")
    private CategoryDto category;
    private String state;
    @NotNull(message = "Поле локация события не может быть пустым.")
    private LocationDto location;
    @NotBlank(message = "Дата проведения события не может быть пустой.")
    private String eventDate; //yyyy-MM-dd HH:mm:ss
    private String publishedOn;	// Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private String createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @NotNull(message = "Поле инициатор не может быть пустым.")
    private UserShortDto initiator;
    @NotNull(message = "Поле оплата не может быть пустым.")
    private boolean paid;
    private long participantLimit; //0 - нет ограничений
    private boolean requestModeration = true; //Нужна ли пре-модерация заявок на участие
    private long confirmedRequests;
    private long views;
}
