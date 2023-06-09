package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.ClosedIssueKafkaResponse;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 Repository issue 정보를 kafka로 부터 받아와 처리하는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaIssueConsumer {
    private final GitRepoService gitRepoService;
    private final ObjectMapper objectMapper;

    @SneakyThrows(JsonProcessingException.class)
    @KafkaListener(topics = "gitrank.to.backend.issues", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        gitRepoService.updateClosedIssues(objectMapper.readValue(message, ClosedIssueKafkaResponse.class));
    }
}
