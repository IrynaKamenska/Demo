package de.neuefische.backend;

import lombok.With;

@With
public record Isbn(String type, String identifier) {
}
