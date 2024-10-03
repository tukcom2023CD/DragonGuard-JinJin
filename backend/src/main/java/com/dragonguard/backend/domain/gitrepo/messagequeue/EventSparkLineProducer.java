package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 레포지토리 스파크라인 kafka 요청을 처리하는 consumer 클래스
 */
@Component
@RequiredArgsConstructor
public class EventSparkLineProducer implements EventProducer<SparkLineEvent> {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void send(final SparkLineEvent request) {
        eventPublisher.publishEvent(request);
    }
}
