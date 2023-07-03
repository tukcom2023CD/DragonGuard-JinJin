package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaGitRepoInfoConsumerImpl implements KafkaConsumer<GitRepoInfoRequest> {
    @Override
    public void consume(String message, Acknowledgment acknowledgment) {}

    @Override
    public GitRepoInfoRequest readValue(String message) {
        return null;
    }
}
