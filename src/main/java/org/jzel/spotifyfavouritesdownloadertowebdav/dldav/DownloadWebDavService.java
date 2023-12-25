package org.jzel.spotifyfavouritesdownloadertowebdav.dldav;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

@Service
@AllArgsConstructor
public class DownloadWebDavService {
    public static final String LQ = "128";
    public static final String HQ = "flac";
    public static final String LQ_FILE_ENDING_REGEX = "\\.mp3$";
    public static final String HQ_FILE_ENDING = ".flac";

    private final ExecutorService downloadExecutor;
    private final ExecutorService davExecutor;
    private final Terminal terminal;

    public void downloadTrack(String trackId) {
        downloadExecutor.submit(() -> {
            downloadTrackWithDeemix(trackId, HQ);
            davExecutor.submit(this::uploadToWebDav);
        });
    }

    public void deleteDownloadedTrack(String trackId) {
        downloadExecutor.submit(() -> {
            downloadTrackWithDeemix(trackId, LQ);
            davExecutor.submit(this::deleteDownloadedLQTracksFromDav);
        });
    }

    private void deleteDownloadedLQTracksFromDav() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of("./tmp"))) {
            stream.forEach(filePath -> {
                terminal.execute(String.format("rclone delete MYWEBDAV:/\"%s\"", filePath.getFileName().toString().replaceAll(
                    LQ_FILE_ENDING_REGEX, HQ_FILE_ENDING)));
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadTrackWithDeemix(String trackId, String quality) {
        terminal.execute(String.format("deemix --portable -b %s -p ./tmp https://open.spotify.com/track/%s", quality, trackId));
    }

    private void uploadToWebDav() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of("./tmp"))) {
            stream.forEach(filePath -> {
                terminal.execute(String.format("rclone copy \"%s\" MYWEBDAV:/", filePath));
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
