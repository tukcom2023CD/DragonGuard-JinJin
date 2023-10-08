package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.email.dto.kafka.KafkaEmail;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaEmailConsumerImpl implements KafkaConsumer<KafkaEmail> {
    @Override
    public void consume(String message, Acknowledgment acknowledgment) {}

    @Override
    public KafkaEmail readValue(String message) {
        return null;
    }
}
