package com.dragonguard.backend.global.template.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.support.Acknowledgment;

/**
 * @author 김승진
 * @description Kafka Consumer 의 공통 기능을 뽑아낸 인터페이스
 */
public interface KafkaConsumer {
    void consume(final String message, final Acknowledgment acknowledgment)
            throws JsonProcessingException;
}
