package ru.practicum.ewm_main_service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.compilation.dto.CompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.NewCompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm_main_service.compilation.service.CompilationService;
import ru.practicum.ewm_main_service.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addNewCompilation(@Valid @RequestBody NewCompilationDto dto) {
        log.info("Получен запрос на добавление подборки событий {}", dto.getTitle());
        return compilationService.addNewCompilation(dto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос на обновление информации о подборке событий {}", updateCompilationRequest.getTitle());
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Получен запрос на удаление информации о подборке событий с ID {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilationsPublic(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка подборок событий по {} на странице {}", size, from);

        if ((from < 0) || (size < 1)) {
            throw new ValidationException("Неверные параметры запроса");
        }
        return compilationService.getCompilationsPublic(pinned,from / size, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationByIdPublic(@PathVariable Long compId) {
        log.info("Получен запрос на получение подборки событий с ID {}", compId);
        return compilationService.getCompilationByIdPublic(compId);
    }
}
