package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends MongoRepository<Track, String> {
}
