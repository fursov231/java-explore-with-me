package ru.practicum.ewm;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.ErrorResponse;
import ru.practicum.ewm.exception.InternalServerError;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;

@RestControllerAdvice("ru.practicum.ewm")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValidException(final ValidationException e) {
        return new ErrorResponse(e.toString(),
                "Only pending or canceled events can be changed",
                "For the requested operation the conditions are not met.",
                "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e, long id) {
        return new ErrorResponse(e.toString(),
                "Event with id=" + id + "was not found.",
                "The required object was not found.",
                "NOT_FOUND");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final NotFoundException e, long id) {
        return new ErrorResponse(e.toString(),
                e.getMessage(),
                "Integrity constraint has been violated",
                "CONFLICT");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnsupportedStatusException(final InternalServerError e) {
        return new ErrorResponse(e.toString(),
                e.getMessage(),
                "Error occurred",
                "INTERNAL_SERVER_ERROR");
    }
}
