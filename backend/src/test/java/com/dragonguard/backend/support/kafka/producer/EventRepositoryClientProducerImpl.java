package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.member.dto.kafka.RepositoryEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EventRepositoryClientProducerImpl implements EventProducer<RepositoryEvent> {
    @Override
    public void send(RepositoryEvent request) {}
}
