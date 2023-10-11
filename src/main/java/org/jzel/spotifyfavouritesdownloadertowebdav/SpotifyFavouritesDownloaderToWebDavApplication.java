package org.jzel.spotifyfavouritesdownloadertowebdav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories(basePackages = "org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs")
public class SpotifyFavouritesDownloaderToWebDavApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyFavouritesDownloaderToWebDavApplication.class, args);
    }

}
