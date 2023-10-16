package com.dragonguard.backend.global.template.kafka;

import org.springframework.kafka.support.Acknowledgment;

/**
 * @author 김승진
 * @description Kafka Consumer 의 공통 기능을 뽑아낸 인터페이스
 */

public interface KafkaConsumer<T> {
    void consume(final T message, final Acknowledgment acknowledgment);
}
