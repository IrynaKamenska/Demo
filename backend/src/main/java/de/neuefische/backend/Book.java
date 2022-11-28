package de.neuefische.backend;
import lombok.With;

@With
public record Book(
        String id,
        VolumeInfo volumeInfo

) {
    
}
