package ru.practicum.ewm.exception;

public class InternalServerError extends RuntimeException {
    public InternalServerError(final String message) {
        super(message);
    }
}
