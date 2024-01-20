package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Getter
public class Auth {
    private Optional<String> accessToken = Optional.empty();
    private Optional<String> refreshToken = Optional.empty();

    public void setAccessToken(String accessToken) {
        this.accessToken = Optional.of(accessToken);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = Optional.of(refreshToken);
    }
    public void resetRefreshToken() {
        this.refreshToken = Optional.empty();
    }
}
