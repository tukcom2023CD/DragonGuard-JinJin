package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(KafkaRepositoryRequest request) {
        kafkaTemplate.send("gitrank.to.backend.repository.client", "repository", request);
    }
}
