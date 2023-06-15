package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaGitRepoInfoProducerImpl  implements KafkaProducer<GitRepoRequest> {
    @Override
    public void send(GitRepoRequest request) {}
}
