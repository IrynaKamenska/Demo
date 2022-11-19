package de.neuefische.backend;

public record Book(String id, String title, String author, String isbn, BookState bookState) {
}
