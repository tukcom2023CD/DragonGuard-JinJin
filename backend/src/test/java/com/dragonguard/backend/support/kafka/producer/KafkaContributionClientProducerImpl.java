package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.member.dto.kafka.KafkaContributionRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaContributionClientProducerImpl implements KafkaProducer<KafkaContributionRequest> {
    @Override
    public void send(KafkaContributionRequest request) {}
}
