package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;

@RestControllerAdvice("ru.practicum.ewm")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValidException(final ValidationException e) {
        return new ApiError(new ArrayList<>(Collections.singleton(e.getMessage())),
                "Only pending or canceled events can be changed",
                "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e, long id) {
        return new ApiError(new ArrayList<>(Collections.singleton(e.getMessage())),
                "Event with id=" + id + "was not found.",
                "The required object was not found.",
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAuthorizationException(final ForbiddenException e) {
        return new ApiError(new ArrayList<>(Collections.singleton(e.getMessage())),
                "Only pending or canceled events can be changed",
                "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        return new ApiError(new ArrayList<>(Collections.singleton(e.getMessage())),
                e.getMessage(),
                "Integrity constraint has been violated",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnsupportedStatusException(final InternalServerError e) {
        return new ApiError(new ArrayList<>(Collections.singleton(e.getMessage())),
                e.getMessage(),
                "Error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
