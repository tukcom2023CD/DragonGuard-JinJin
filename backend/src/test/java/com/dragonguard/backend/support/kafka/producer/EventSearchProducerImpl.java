package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.search.dto.kafka.SearchEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class EventSearchProducerImpl implements EventProducer<SearchEvent> {
    @Override
    public void send(SearchEvent request) {}
}
