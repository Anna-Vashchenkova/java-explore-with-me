package ru.practicum.ewm_main_service.exception;

public class DataAlreadyExists extends RuntimeException {
    public DataAlreadyExists(String message) {
        super(message);
    }

}
