package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.KafkaContributionRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description Kafka로 기여도 조회를 위한 요청을 보내는 Producer
 */

@Component
@RequiredArgsConstructor
public class KafkaContributionClientProducer implements KafkaProducer<KafkaContributionRequest> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(KafkaContributionRequest request) {
        kafkaTemplate.send("gitrank.to.backend.contribution.client", "contribution", request);
    }
}
