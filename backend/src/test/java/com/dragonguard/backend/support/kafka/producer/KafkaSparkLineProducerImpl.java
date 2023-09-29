package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaSparkLineProducerImpl implements KafkaProducer<SparkLineKafka> {
    @Override
    public void send(SparkLineKafka request) {}
}
