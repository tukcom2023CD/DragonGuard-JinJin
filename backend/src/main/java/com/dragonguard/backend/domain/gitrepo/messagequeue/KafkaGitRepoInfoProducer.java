package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaGitRepoInfoProducer implements KafkaProducer<GitRepoRequest> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public void send(GitRepoRequest request) {
        kafkaTemplate.send("gitrank.to.backend.git-repos-info", "git-repos-info", request);
    }
}
