package ru.practicum.ewm.error.exeptions;

public class ConflictException extends RuntimeException {
    public static final Exception e = new RuntimeException("The required object was not found.");

    public ConflictException(String message) {
        super(message, e);
    }
}