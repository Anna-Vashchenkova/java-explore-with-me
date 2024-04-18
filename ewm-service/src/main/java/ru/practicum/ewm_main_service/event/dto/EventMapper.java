package ru.practicum.ewm_main_service.event.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.category.dto.CategoryMapper;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.user.dto.UserMapper;
import ru.practicum.statistics_client.StatisticsClient;
import ru.practicum.statistics_dto.HitOutcomeDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventMapper {
    private final StatisticsClient client;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto toEventFullDto(Event entity) {
        Optional<HitOutcomeDto> hitOutcomeDto = Objects.requireNonNull(client.getStat(entity.getCreatedOn(), LocalDateTime.now(), List.of("/events/" + entity.getId()), true)
                .block()).stream().findFirst();
        long count = 0;
        if (hitOutcomeDto.isPresent()) {

            count = hitOutcomeDto.get().getHits();
        }
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
                .views(count)
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
