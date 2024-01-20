package org.jzel.spotifyfavouritesdownloadertowebdav;

import org.jzel.spotifyfavouritesdownloadertowebdav.mailalert.EmailSenderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories(basePackages = "org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs")
public class SpotifyFavouritesDownloaderToWebDavApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpotifyFavouritesDownloaderToWebDavApplication.class,
            args);

        context.getBean(EmailSenderService.class).sendStartupAlert();
    }

}
