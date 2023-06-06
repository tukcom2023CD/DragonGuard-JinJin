package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 kafka로 부터 받아와 처리하는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaGitRepoConsumer {
    private final GitRepoMemberService gitRepoMemberService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "gitrank.to.backend.git-repos", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<String, Map<String, Object>> map = null;

        try {
            map = objectMapper.readValue(message, new TypeReference<Map<String, Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {}

        if (Objects.isNull(map)) return;

        List<GitRepoMemberResponse> list = new ArrayList<>();
        String gitRepo = null;

        for (Object key :  map.keySet()) {
            Map<String, Object> resultMap = map.get(key);
            list.add(new GitRepoMemberResponse((String)key, (Integer)resultMap.get("commits"), (Integer)resultMap.get("addition"), (Integer)resultMap.get("deletion")));
            gitRepo = (String) resultMap.get("gitRepo");
        }
        gitRepoMemberService.updateOrSaveAll(list, gitRepo);
    }
}
