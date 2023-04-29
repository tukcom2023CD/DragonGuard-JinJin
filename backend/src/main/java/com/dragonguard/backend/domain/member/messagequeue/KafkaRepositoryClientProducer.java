package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.request.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.util.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaRepositoryClientProducer implements KafkaProducer<KafkaRepositoryRequest> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(KafkaRepositoryRequest request) {
        kafkaTemplate.send("gitrank.to.backend.repository.client", "repository", request);
    }
}
