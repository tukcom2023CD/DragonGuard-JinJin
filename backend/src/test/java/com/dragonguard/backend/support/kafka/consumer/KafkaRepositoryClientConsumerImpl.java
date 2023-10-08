package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.member.dto.kafka.RepositoryClientResponse;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaRepositoryClientConsumerImpl implements KafkaConsumer<RepositoryClientResponse> {
    @Override
    public void consume(String message, Acknowledgment acknowledgment) {}

    @Override
    public RepositoryClientResponse readValue(String message) {
        return null;
    }
}
