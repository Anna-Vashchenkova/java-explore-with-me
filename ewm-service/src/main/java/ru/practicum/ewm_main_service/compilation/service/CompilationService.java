package ru.practicum.ewm_main_service.compilation.service;

import ru.practicum.ewm_main_service.compilation.dto.CompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.NewCompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto addNewCompilation(NewCompilationDto dto);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilationsPublic(Boolean pinned, Integer from, Integer size);
}
