package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 레포지토리 스파크라인 kafka 요청을 처리하는 consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaSparkLineProducer implements KafkaProducer<SparkLineKafka> {
    private static final String TOPIC = "gitrank.to.backend.spark-line";
    private static final String KEY = "spark-line";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public void send(final SparkLineKafka request) {
        kafkaTemplate.send(TOPIC, KEY, request);
    }
}
