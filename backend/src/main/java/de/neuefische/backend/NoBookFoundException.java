package de.neuefische.backend;

public class NoBookFoundException extends RuntimeException {
public NoBookFoundException(String message){
    super(message);
}
}
