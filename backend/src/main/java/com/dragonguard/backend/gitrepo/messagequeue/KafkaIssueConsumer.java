package com.dragonguard.backend.gitrepo.messagequeue;

import com.dragonguard.backend.gitrepo.service.GitRepoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 Repository issue 정보를 kafka로 부터 받아와 처리하는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaIssueConsumer {
    private final GitRepoService gitRepoService;

    @KafkaListener(topics = "gitrank.to.backend.issues", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (map.isEmpty()) {
            return;
        }
        String name = (String) map.get("name");
        Integer closedIssue = (Integer) map.get("closedIssue");
        gitRepoService.updateClosedIssues(name, closedIssue);
    }
}
