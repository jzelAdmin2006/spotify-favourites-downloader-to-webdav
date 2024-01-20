package org.jzel.spotifyfavouritesdownloadertowebdav.mailalert;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailSenderService {
  private final JavaMailSender mailSender;
  private final MailConfig mailConfig;

  @EventListener(ApplicationReadyEvent.class)
  public void sendStartupAlert() {
    send("\uD83D\uDC31 Spotify Favourites Downloader to WebDav gestartet (Erstanmeldung erforderlich)!",
        """
            Der Spotify Favourites Downloader to WebDav Server wurde gestartet.
            
            Bitte unter folgendem Link anmelden:
            
            """ + mailConfig.getReauthUrl());
  }

  public void sendReauthAlert() {
    send("\uD83D\uDE40 Spotify Favourites Downloader to WebDav Neuanmeldung erforderlich!",
        """
            Die Erneuerung des Spotify Access Tokens ist fehlgeschlagen.
            Die Spotify Favourites Downloader to WebDav App benötigt eine erneute Anmeldung bei Spotify.
            
            Bitte unter folgendem Link anmelden:
            
            """ + mailConfig.getReauthUrl());
  }

  private void send(String subject, String text) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(mailConfig.getUsername());
    msg.setTo(mailConfig.getTo());
    msg.setText(text);
    msg.setSubject(subject);

    mailSender.send(msg);
  }
}
