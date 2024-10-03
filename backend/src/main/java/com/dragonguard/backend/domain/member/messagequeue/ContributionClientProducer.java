package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.ContributionEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description Kafka로 기여도 조회를 위한 요청을 보내는 Producer
 */
@Component
@RequiredArgsConstructor
public class ContributionClientProducer implements EventProducer<ContributionEvent> {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void send(final ContributionEvent event) {
        eventPublisher.publishEvent(event);
    }
}
