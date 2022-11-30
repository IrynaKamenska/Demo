package de.neuefische.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BookResponseElement(@JsonProperty("items") List<Book> bookItems) {
}
