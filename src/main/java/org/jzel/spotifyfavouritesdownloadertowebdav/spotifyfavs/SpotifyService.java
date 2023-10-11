package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jzel.spotifyfavouritesdownloadertowebdav.dldav.DownloadWebDavService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SpotifyService {
    private static final String SPOTIFY_API_URL = "https://api.spotify.com/v1/me/tracks";

    private final Auth auth;
    private final TrackRepository trackRepository;
    private final DownloadWebDavService dlToDavService;

    public List<String> getFavoriteTrackIds(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        List<String> trackIds = new ArrayList<>();
        int limit = 50;
        int offset = 0;

        while (true) {
            String url = SPOTIFY_API_URL + "?limit=" + limit + "&offset=" + offset;

            Request request = new Request.Builder().url(url)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray items = jsonResponse.getJSONArray("items");

                    if (items.length() == 0) {
                        break;
                    }

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject track = items.getJSONObject(i).getJSONObject("track");
                        trackIds.add(track.getString("id"));
                    }

                    offset += limit;
                } else {
                    throw new RuntimeException("Failed to fetch tracks from Spotify: " + response.message());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return trackIds;
    }

    /**
     * @deprecated Only call this manually for testing purposes
     */
    @Deprecated
    @Scheduled(fixedRate = 12 * 3600 * 1000)
    void dailyUpdateFavourites() {
        if (auth.getAccessToken().isPresent()) {
            List<String> previousFavoriteTrackIds = trackRepository.findAll().stream().map(Track::getId).toList();
            List<String> favoriteTrackIds = getFavoriteTrackIds(auth.getAccessToken().orElseThrow());

            List<String> newFavoriteTrackIds = favoriteTrackIds.stream()
                    .filter(id -> !previousFavoriteTrackIds.contains(id))
                    .toList();
            List<String> removedFavoriteTrackIds = previousFavoriteTrackIds.stream()
                    .filter(id -> !favoriteTrackIds.contains(id))
                    .toList();

            updateFavTracklistPersistence(newFavoriteTrackIds, removedFavoriteTrackIds);

            newFavoriteTrackIds.forEach(dlToDavService::downloadTrack);
            removedFavoriteTrackIds.forEach(dlToDavService::deleteDownloadedTrack);
        }
    }

    private void updateFavTracklistPersistence(List<String> newFavoriteTrackIds, List<String> removedFavoriteTrackIds) {
        trackRepository.saveAll(newFavoriteTrackIds.stream().map(Track::new).toList());
        trackRepository.deleteAllById(removedFavoriteTrackIds);
    }
}
