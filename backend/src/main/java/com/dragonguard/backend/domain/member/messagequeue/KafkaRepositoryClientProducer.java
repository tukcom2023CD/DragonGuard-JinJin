package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description Kafka로 멤버의 Repository 조회를 위한 요청을 보내는 Producer
 */
@Component
@RequiredArgsConstructor
public class KafkaRepositoryClientProducer implements KafkaProducer<KafkaRepositoryRequest> {
    private static final String TOPIC = "gitrank.to.backend.repository.client";
    private static final String KEY = "repository";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(final KafkaRepositoryRequest request) {
        kafkaTemplate.send(TOPIC, KEY, request);
    }
}
