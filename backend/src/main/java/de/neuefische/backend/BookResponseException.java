package de.neuefische.backend;

public class BookResponseException extends RuntimeException {
    public BookResponseException(String message) {
        super(message);
    }
}
