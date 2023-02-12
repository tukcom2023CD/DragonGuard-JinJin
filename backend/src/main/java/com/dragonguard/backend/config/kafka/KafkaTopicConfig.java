package com.dragonguard.backend.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic commitTopic() {
        return TopicBuilder
                .name("gitrank.to.scrape.commit")
                .build();
    }

    @Bean
    public NewTopic resultTopic() {
        return TopicBuilder
                .name("gitrank.to.scrape.result")
                .build();
    }

    @Bean
    public NewTopic gitRepoTopic() {
        return TopicBuilder
                .name("gitrank.to.scrape.git-repos")
                .build();
    }
}
