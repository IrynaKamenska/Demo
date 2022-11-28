package de.neuefische.backend;

public class BookAlreadyExistException extends RuntimeException{
    public BookAlreadyExistException(String e) {
        super(e);
    }
}
