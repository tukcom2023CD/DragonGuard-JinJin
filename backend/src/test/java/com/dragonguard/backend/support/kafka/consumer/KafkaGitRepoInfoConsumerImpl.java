package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaGitRepoInfoConsumerImpl implements KafkaConsumer<GitRepoRequest> {
    @Override
    public void consume(String message) {}

    @Override
    public GitRepoRequest readValue(String message) {
        return null;
    }
}
