package com.dragonguard.backend.gitrepo.messagequeue;

import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.service.GitRepoMemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaGitRepoConsumer {

    private final GitRepoMemberService gitRepoMemberService;

    @KafkaListener(topics = "gitrank.to.backend.git-repos", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<String, Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (map.isEmpty()) {
            return;
        }
        List<GitRepoMemberResponse> list = new ArrayList<>();
        String gitRepo = null;

        for (Object key :  map.keySet()) {
            Map<String, Object> resultMap = map.get(key);
            list.add(new GitRepoMemberResponse((String)key, (Integer)resultMap.get("commits"), (Integer)resultMap.get("addition"), (Integer)resultMap.get("deletion")));
            gitRepo = (String)resultMap.get("gitRepo");
        }
        gitRepoMemberService.updateOrSaveAll(list, gitRepo);
    }
}
