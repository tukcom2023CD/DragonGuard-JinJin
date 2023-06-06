package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author 김승진
 * @description 깃허브 Repository issue 정보를 kafka로 부터 받아와 처리하는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaIssueConsumer {
    private final GitRepoService gitRepoService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "gitrank.to.backend.issues", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<String, Object> map = null;

        try {
            map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {}

        if (Objects.isNull(map)) return;

        String name = (String) map.get("name");
        Integer closedIssue = (Integer) map.get("closedIssue");
        gitRepoService.updateClosedIssues(name, closedIssue);
    }
}
