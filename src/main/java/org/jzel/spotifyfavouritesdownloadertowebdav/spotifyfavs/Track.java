package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tracks")
@Data
public class Track {
    @Id
    private final String id;
}
