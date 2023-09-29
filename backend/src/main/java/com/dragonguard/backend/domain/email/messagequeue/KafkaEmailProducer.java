package com.dragonguard.backend.domain.email.messagequeue;

import com.dragonguard.backend.domain.email.dto.kafka.KafkaEmail;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description Kafka로 이메일을 보내기 위한 요청을 보내는 Producer
 */

@Component
@RequiredArgsConstructor
public class KafkaEmailProducer implements KafkaProducer<KafkaEmail> {
    private static final String TOPIC = "gitrank.to.backend.email";
    private static final String KEY = "email";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public void send(final KafkaEmail request) {
        kafkaTemplate.send(TOPIC, KEY, request);
    }
}
