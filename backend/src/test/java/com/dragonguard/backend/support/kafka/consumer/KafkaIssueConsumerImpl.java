package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.ClosedIssueKafkaResponse;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaIssueConsumerImpl implements KafkaConsumer<ClosedIssueKafkaResponse> {
    @Override
    public void consume(String message, Acknowledgment acknowledgment) {}

    @Override
    public ClosedIssueKafkaResponse readValue(String message) {
        return null;
    }
}
