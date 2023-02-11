package com.dragonguard.backend.gitrepo.messagequeue;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaGitRepoProducer implements KafkaProducer<GitRepoRequest> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(GitRepoRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.git-repos", "git-repos", request);
    }
}
