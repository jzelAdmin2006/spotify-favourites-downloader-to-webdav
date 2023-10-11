package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spotify")
@Data
public class SpotifyConfig {
    private String clientId;
    private String clientSecret;
    private String redirect;
}
