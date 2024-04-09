package ru.practicum.ewm_main_service.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.category.dto.CategoryMapper;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.user.dto.UserMapper;

import java.time.format.DateTimeFormatter;

@UtilityClass
public final class EventMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventFullDto(Event entity) {
        return EventFullDto.builder()
                .id(entity.getId())
                .annotation(entity.getAnnotation())
                .category(CategoryMapper.toCategoryDto(entity.getCategory()))
                .confirmedRequests(entity.getConfirmedRequests())
                .createdOn(entity.getCreatedOn().format(formatter))
                .description(entity.getDescription())
                .eventDate(entity.getEventDate().format(formatter))
                .initiator(UserMapper.toUserShortDto(entity.getInitiator()))
                .location(new LocationDto(entity.getLocation().getLat(), entity.getLocation().getLon()))
                .paid(entity.isPaid())
                .participantLimit(entity.getParticipantLimit())
                .publishedOn(entity.getPublishedOn() != null ? entity.getPublishedOn().format(formatter) : null)
                .requestModeration(entity.isRequestModeration())
                .state(entity.getState().name())
                .title(entity.getTitle())
                .views(entity.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event entity) {
        return EventShortDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .annotation(entity.getAnnotation())
                .category(CategoryMapper.toCategoryDto(entity.getCategory()))
                .eventDate(entity.getEventDate().format(formatter))
                .initiator(UserMapper.toUserShortDto(entity.getInitiator()))
                .paid(entity.isPaid())
                .confirmedRequests(entity.getConfirmedRequests())
                .views(entity.getViews())
                .build();
    }
}
