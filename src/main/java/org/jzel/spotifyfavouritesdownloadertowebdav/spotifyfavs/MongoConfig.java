package org.jzel.spotifyfavouritesdownloadertowebdav.spotifyfavs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Data
public class MongoConfig {
    private String host;
    private String username;
    private String password;
    private String database;

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(String.format("mongodb+srv://%s:%s@%s/%s", username, password, host, database));
    }
}
