package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EventSparkLineProducerImpl implements EventProducer<SparkLineEvent> {
    @Override
    public void send(SparkLineEvent request) {}
}
