package ru.practicum.ewm.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.error.exeptions.ConflictException;
import ru.practicum.ewm.error.exeptions.NotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason(e.getCause().toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason(e.getCause().toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}