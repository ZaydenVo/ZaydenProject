package com.zayden.post_service.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;

@Configuration
public class MongoIndexConfig {
    @Bean
    CommandLineRunner initIndexes(MongoTemplate mongoTemplate) {
        return args -> {
            TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                    .onField("title")
                    .onField("content")
                    .build();

            mongoTemplate.indexOps("post").createIndex(textIndex);
        };
    }
}
