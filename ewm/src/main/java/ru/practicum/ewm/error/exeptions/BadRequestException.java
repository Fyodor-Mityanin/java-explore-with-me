package ru.practicum.ewm.error.exeptions;

public class BadRequestException extends RuntimeException {
    public static final Exception e = new RuntimeException("The required object was not found.");

    public BadRequestException(String message) {
        super(message, e);
    }
}