package ru.practicum.ewm_main_service.config;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private List <String> errors;
    private String message;
    private String reason;
    private String status; //Код статуса HTTP-ответа
    private String timestamp;

    public ApiError(List<String> errors, String message, String reason, String status) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();    }
}
