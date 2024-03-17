package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaGitRepoInfoProducerImpl implements KafkaProducer<GitRepoInfoRequest> {
    @Override
    public void send(GitRepoInfoRequest request) {}
}
