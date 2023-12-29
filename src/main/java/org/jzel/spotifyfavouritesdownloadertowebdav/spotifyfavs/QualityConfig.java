package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "quality")
@Data
public class QualityConfig {
    private boolean flacMode;
}
