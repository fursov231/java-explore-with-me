package ru.practicum.ewm.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    private final List<String> error;
    private final String message;
    private final String reason;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public ApiError(List<String> error, String message, String reason, HttpStatus status) {
        this.error = error;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

}