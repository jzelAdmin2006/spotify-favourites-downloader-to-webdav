package org.jzel.spotifyfavouritesdownloadertowebdav.dldav;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ServiceConfig {
    @Bean
    ExecutorService downloadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    ExecutorService davExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}