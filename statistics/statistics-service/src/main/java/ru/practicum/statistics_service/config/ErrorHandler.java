package ru.practicum.statistics_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.statistics_service.exception.DataNotFoundException;
import ru.practicum.statistics_service.exception.ValidationException;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEmail(final ValidationException e) {
        log.info("Ошибка валидации",e);
        return new ErrorResponse(
                e.getMessage(), e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidEmail(final DataNotFoundException e) {
        log.info("Ошибка валидации", e);
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidEmail(final EntityNotFoundException e) {
        log.info("Ошибка валидации");
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }
}
