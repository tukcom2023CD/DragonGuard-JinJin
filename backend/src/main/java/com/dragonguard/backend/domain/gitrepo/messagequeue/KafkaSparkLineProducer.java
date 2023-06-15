package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSparkLineProducer implements KafkaProducer<SparkLineKafka> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public void send(SparkLineKafka request) {
        kafkaTemplate.send("gitrank.to.backend.spark-line", "spark-line", request);
    }
}
