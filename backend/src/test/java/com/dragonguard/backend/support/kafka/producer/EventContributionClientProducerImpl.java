package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.member.dto.kafka.ContributionEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EventContributionClientProducerImpl implements EventProducer<ContributionEvent> {
    @Override
    public void send(ContributionEvent request) {}
}
