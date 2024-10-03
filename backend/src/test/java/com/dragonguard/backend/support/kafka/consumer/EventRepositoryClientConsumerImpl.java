package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.global.template.kafka.EventConsumer;

import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EventRepositoryClientConsumerImpl implements EventConsumer {
    @Override
    public void consume(String message, Acknowledgment acknowledgment) {}
}
