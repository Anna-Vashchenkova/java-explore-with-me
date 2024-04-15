package ru.practicum.ewm_main_service.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.compilation.Compilation;
import ru.practicum.ewm_main_service.event.dto.EventMapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto().builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned() != null ? compilation.getPinned() : null)
                .title(compilation.getTitle())
                .events(compilation.getEvents() != null ? compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
