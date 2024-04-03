package ru.practicum.ewm_main_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm_main_service.exception.DataAlreadyExists;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;
import ru.practicum.ewm_main_service.exception.ValidationException;

import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidEmail(final ValidationException e) {
        log.info("Ошибка валидации");
        return new ApiError(
                Collections.singletonList(e.getMessage()),
                e.getMessage(),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.toString()
                );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleInvalidEmail(final DataNotFoundException e) {
        log.info("Ошибка - данные не найдены");
        return new ApiError(
                Collections.singletonList(e.getMessage()),
                e.getMessage(),
                "Not Found",
                HttpStatus.NOT_FOUND.toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidEmail(final DataAlreadyExists e) {
        log.info("Ошибка");
        return new ApiError(
                Collections.singletonList(e.getMessage()),
                e.getMessage(),
                "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.toString()
        );
    }
}
