package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.search.dto.kafka.KafkaSearchRequest;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class KafkaSearchProducerImpl implements KafkaProducer<KafkaSearchRequest> {
    @Override
    public void send(KafkaSearchRequest request) {}
}
