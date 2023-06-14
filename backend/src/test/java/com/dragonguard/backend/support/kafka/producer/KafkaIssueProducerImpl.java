package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaIssueProducerImpl implements KafkaProducer<GitRepoNameRequest> {
    @Override
    public void send(GitRepoNameRequest request) {}
}
