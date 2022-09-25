package ru.practicum.ewm.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private final String error;
    private final String message;
    private final String reason;
    private final String status;
    private final LocalDateTime timestamp;

    public ErrorResponse(String error, String message, String reason, String status) {
        this.error = error;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

}