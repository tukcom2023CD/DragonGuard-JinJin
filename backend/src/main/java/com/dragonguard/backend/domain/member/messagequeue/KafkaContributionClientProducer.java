package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.request.kafka.KafkaContributionRequest;
import com.dragonguard.backend.util.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaContributionClientProducer implements KafkaProducer<KafkaContributionRequest> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(KafkaContributionRequest request) {
        kafkaTemplate.send("gitrank.to.backend.contribution", "contribution", request);
    }
}
