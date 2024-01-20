package org.jzel.spotifyfavouritesdownloadertowebdav.mailalert;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class MailConfig {
    private String username;
    private String to;
    private String reauthUrl;
}
