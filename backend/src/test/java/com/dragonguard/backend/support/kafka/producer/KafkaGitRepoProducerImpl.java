package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoRequest;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaGitRepoProducerImpl implements KafkaProducer<GitRepoRequest> {
    @Override
    public void send(GitRepoRequest request) {}
}
