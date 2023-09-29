package com.dragonguard.backend.global.template.kafka;

/**
 * @author 김승진
 * @description Kafka Consumer 의 공통 기능을 뽑아낸 인터페이스
 */

public interface KafkaConsumer<T> {
    void consume(final String message);
    T readValue(final String message);
}
