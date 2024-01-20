package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import kotlin.Pair;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/spotify")
@AllArgsConstructor
public class SpotifyController {

    private final AuthService spotifyAuthService;
    private final Auth auth;

    @GetMapping("/token")
    public ResponseEntity<Void> initiateAuthentication() {
        URI spotifyAuthURI = spotifyAuthService.getAuthenticationURI();
        return ResponseEntity.status(HttpStatus.FOUND).location(spotifyAuthURI).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<ClassPathResource> callback(@RequestParam String code) {
        Pair<String, String> tokens = spotifyAuthService.getTokensFromCode(code);
        auth.setAccessToken(tokens.getFirst());
        auth.setRefreshToken(tokens.getSecond());
        return ResponseEntity.ok().body(new ClassPathResource("static/login-success.html"));
    }
}
