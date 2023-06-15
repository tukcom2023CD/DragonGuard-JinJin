package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSparkLineConsumer implements KafkaConsumer<SparkLineKafka> {
    private final ObjectMapper objectMapper;
    private final GitRepoService gitRepoService;

    @Override
    @KafkaListener(topics = "gitrank.to.backend.spark-line", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        SparkLineKafka sparkLine = readValue(message);
        gitRepoService.updateSparkLine(sparkLine.getName(), sparkLine.getGithubToken());
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public SparkLineKafka readValue(String message) {
        return objectMapper.readValue(message, SparkLineKafka.class);
    }
}
