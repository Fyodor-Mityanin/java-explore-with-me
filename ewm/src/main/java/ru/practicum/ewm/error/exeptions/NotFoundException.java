package ru.practicum.ewm.error.exeptions;

public class NotFoundException extends RuntimeException {
    public static final Exception e = new RuntimeException("The required object was not found.");

    public NotFoundException(String message) {
        super(message, e);
    }
}