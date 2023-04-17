package com.dragonguard.backend.member.messagequeue;


import com.dragonguard.backend.util.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaRepositoryProducer implements KafkaProducer<String> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String request) {
        kafkaTemplate.send("gitrank.to.backend.repository", "repository", request);
    }
}
