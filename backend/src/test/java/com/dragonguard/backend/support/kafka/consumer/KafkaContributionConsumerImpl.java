package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.contribution.dto.kafka.ContributionKafkaResponse;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaContributionConsumerImpl implements KafkaConsumer<ContributionKafkaResponse> {
    @Override
    public void consume(String message) {}

    @Override
    public ContributionKafkaResponse readValue(String message) {
        return null;
    }
}
