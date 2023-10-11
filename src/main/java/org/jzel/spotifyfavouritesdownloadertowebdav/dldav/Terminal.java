package org.jzel.spotifyfavouritesdownloadertowebdav.dldav;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class Terminal {

    public void execute(String command) {

        String os = System.getProperty("os.name").toLowerCase();
        String[] cmd;

        if (os.contains("win")) {
            cmd = new String[]{"cmd.exe", "/c", command};
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            cmd = new String[]{"/bin/sh", "-c", command};
        } else {
            throw new UnsupportedOperationException("Unknown operating system");
        }

        try {
            Process process = new ProcessBuilder(cmd).start();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
