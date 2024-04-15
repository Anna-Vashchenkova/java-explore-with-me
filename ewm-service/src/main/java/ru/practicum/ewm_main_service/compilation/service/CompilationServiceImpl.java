package ru.practicum.ewm_main_service.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.compilation.Compilation;
import ru.practicum.ewm_main_service.compilation.dto.CompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.CompilationMapper;
import ru.practicum.ewm_main_service.compilation.dto.NewCompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm_main_service.compilation.repository.CompilationRepository;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventService eventService;

    @Override
    public CompilationDto addNewCompilation(NewCompilationDto dto) {
        List<Event> events = new ArrayList<>();
        if (dto.getEvents() != null) {
            events = dto.getEvents().stream()
                    .map(id -> {
                        return eventService.findEventById(id).get();
                    })
                    .collect(Collectors.toList());
        }
        Compilation compilation = repository.save(new Compilation(null, dto.isPinned(), dto.getTitle(), events));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Подборка событий с ID %S не найдена", compId)));
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            List<Long> eventsIds = updateCompilationRequest.getEvents();
            List<Event> events = eventsIds.stream()
                    .map(id -> {
                        return eventService.findEventById(id).get();
                    })
                    .collect(Collectors.toList());
            compilation.setEvents(events);
        }
        if (compilation.getPinned() != null) {
            compilation.setPinned(compilation.getPinned());
        }
        compilation.setTitle(compilation.getTitle());
        return CompilationMapper.toCompilationDto(repository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            Page<Compilation> compilations = repository.findAll(PageRequest.of(from, size));
            return compilations.isEmpty() ? new ArrayList<>() : compilations.getContent().stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }

        Page<Compilation> allByPinned = repository.findAllByPinned(pinned, PageRequest.of(from, size));
        return allByPinned.isEmpty() ? new ArrayList<>() : allByPinned.getContent().stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
