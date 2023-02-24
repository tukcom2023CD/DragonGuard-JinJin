package com.dragonguard.backend.gitrepo.messagequeue;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaIssueProducer implements KafkaProducer<GitRepoNameRequest> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(GitRepoNameRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.issues", "issues", request);
    }
}
