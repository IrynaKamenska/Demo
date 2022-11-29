package de.neuefische.backend;


import lombok.With;

import java.util.List;
@With
public record VolumeInfo (
        String title,
        List<String> authors,
        List<Isbn> industryIdentifiers,
        ImageLinks imageLinks,
        String previewLink

        )  {
}
