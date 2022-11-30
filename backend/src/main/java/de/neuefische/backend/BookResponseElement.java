package de.neuefische.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.util.List;
@With
public record BookResponseElement(int totalItems, @JsonProperty("items") List<Book> bookItems) {
}
