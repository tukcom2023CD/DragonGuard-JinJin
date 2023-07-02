package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description 레포지토리 스파크라인 kafka 요청을 보내는 consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaSparkLineConsumer implements KafkaConsumer<SparkLineKafka> {
    private final ObjectMapper objectMapper;
    private final GitRepoService gitRepoService;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.spark-line", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message, Acknowledgment acknowledgment) {
        SparkLineKafka sparkLine = readValue(message);
        gitRepoService.updateSparkLine(sparkLine.getId(), sparkLine.getGithubToken());
        acknowledgment.acknowledge();
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public SparkLineKafka readValue(String message) {
        return objectMapper.readValue(message, SparkLineKafka.class);
    }
}
