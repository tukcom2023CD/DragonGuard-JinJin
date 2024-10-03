package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.email.dto.kafka.EmailEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EventEmailProducerImpl implements EventProducer<EmailEvent> {
    @Override
    public void send(EmailEvent request) {}
}
