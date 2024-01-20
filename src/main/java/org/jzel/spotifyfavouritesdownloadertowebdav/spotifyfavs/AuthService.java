package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import kotlin.Pair;
import lombok.AllArgsConstructor;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.jzel.spotifyfavouritesdownloadertowebdav.mailalert.EmailSenderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;

@Service
@AllArgsConstructor
public class AuthService {

    private static final String TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String SCOPES = "user-library-read";

    private final Auth auth;
    private final SpotifyConfig config;
    private final EmailSenderService emailSenderService;

    @Scheduled(fixedRate = 3540 * 1000)
    void hourlyRefresh() {
        if (auth.getRefreshToken().isPresent()) {
            refreshToken();
        }
    }

    private void refreshToken() {
        OkHttpClient client = new OkHttpClient();
        String credentials = Base64.getEncoder()
                .encodeToString((config.getClientId() + ":" + config.getClientSecret()).getBytes());

        FormBody body = new FormBody.Builder().add("grant_type", "refresh_token")
                .add("refresh_token", auth.getRefreshToken().orElseThrow())
                .build();

        Request request = new Request.Builder().url(TOKEN_ENDPOINT)
                .post(body)
                .addHeader("Authorization", "Basic " + credentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                String newAccessToken = jsonResponse.getString("access_token");
                auth.setAccessToken(newAccessToken);
            } else {
                emailSenderService.sendReauthAlert();
                throw new RuntimeException("Failed to refresh token: " + response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public URI getAuthenticationURI() {
        String url = AUTH_URL + "?client_id=" + config.getClientId() + "&response_type=code" + "&redirect_uri="
                + config.getRedirect() + "&scope=" + SCOPES;
        return URI.create(url);
    }

    public Pair<String, String> getTokensFromCode(String code) {
        OkHttpClient client = new OkHttpClient();
        String credentials = Base64.getEncoder()
                .encodeToString((config.getClientId() + ":" + config.getClientSecret()).getBytes());

        FormBody body = new FormBody.Builder().add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", config.getRedirect())
                .build();

        Request request = new Request.Builder().url(TOKEN_ENDPOINT)
                .post(body)
                .addHeader("Authorization", "Basic " + credentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                String accessToken = jsonResponse.getString("access_token");
                String refreshToken = jsonResponse.getString("refresh_token");
                return new Pair<>(accessToken, refreshToken);
            } else {
                throw new RuntimeException("Failed to fetch tokens: " + response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
