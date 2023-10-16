package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description 레포지토리 스파크라인 kafka 요청을 보내는 consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaSparkLineConsumer implements KafkaConsumer {
    private final GitRepoService gitRepoService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.spark-line", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload final String message, final Acknowledgment acknowledgment) throws JsonProcessingException {
        final SparkLineKafka request = objectMapper.readValue(message, SparkLineKafka.class);
        gitRepoService.updateSparkLine(request.getId(), request.getGithubToken());
        acknowledgment.acknowledge();
    }
}
