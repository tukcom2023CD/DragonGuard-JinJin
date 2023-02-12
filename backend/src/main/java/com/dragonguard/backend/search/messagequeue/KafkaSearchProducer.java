package com.dragonguard.backend.search.messagequeue;

import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.search.dto.request.KafkaSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSearchProducer implements KafkaProducer<KafkaSearchRequest> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(KafkaSearchRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.result","result", request);
    }
}
