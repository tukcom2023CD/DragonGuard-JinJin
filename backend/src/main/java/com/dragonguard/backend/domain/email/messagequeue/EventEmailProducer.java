package com.dragonguard.backend.domain.email.messagequeue;

import com.dragonguard.backend.domain.email.dto.kafka.EmailEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description Kafka로 이메일을 보내기 위한 요청을 보내는 Producer
 */
@Component
@RequiredArgsConstructor
public class EventEmailProducer implements EventProducer<EmailEvent> {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void send(final EmailEvent event) {
        eventPublisher.publishEvent(event);
    }
}
