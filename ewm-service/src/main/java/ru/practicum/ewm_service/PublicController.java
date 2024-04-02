package ru.practicum.ewm_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm_service.dto.CompilationDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class PublicController {
    private final PublicService publicService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(
            @RequestParam boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
            ) {
        return null;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable("compId") Long compId) {
        return null;
    }
}
