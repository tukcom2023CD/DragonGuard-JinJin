package com.dragonguard.backend.global.kafka;

public interface KafkaProducer<T> {
    void send(T request);
}
