package com.dragonguard.backend.support.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@TestConfiguration
public class KafkaTopicTestConfig {
    @Bean
    public NewTopic contributionTopic() {
        return TopicBuilder
                .name("gitrank.to.backend.contribution")
                .build();
    }

    @Bean
    public NewTopic resultTopic() {
        return TopicBuilder
                .name("gitrank.to.backend.result")
                .build();
    }

    @Bean
    public NewTopic issuesTopic() {
        return TopicBuilder
                .name("gitrank.to.backend.issues")
                .build();
    }

    @Bean
    public NewTopic gitReposTopic() {
        return TopicBuilder
                .name("gitrank.to.backend.git-repos")
                .build();
    }
}
