package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.email.dto.kafka.KafkaEmail;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaEmailProducerImpl implements KafkaProducer<KafkaEmail> {
    @Override
    public void send(KafkaEmail request) {}
}
