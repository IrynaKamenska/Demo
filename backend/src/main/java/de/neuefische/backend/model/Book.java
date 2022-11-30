package de.neuefische.backend.model;
import lombok.With;

@With
public record Book(
        String id,
        VolumeInfo volumeInfo,
        BookState bookState

) {
    
}
