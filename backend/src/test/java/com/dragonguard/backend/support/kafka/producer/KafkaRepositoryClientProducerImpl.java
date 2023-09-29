package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaRepositoryClientProducerImpl implements KafkaProducer<KafkaRepositoryRequest> {
    @Override
    public void send(KafkaRepositoryRequest request) {}
}
