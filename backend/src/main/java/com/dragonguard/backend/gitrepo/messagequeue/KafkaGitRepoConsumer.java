package com.dragonguard.backend.gitrepo.messagequeue;

import com.dragonguard.backend.gitrepo.dto.response.GitRepoResponse;
import com.dragonguard.backend.gitrepomember.dto.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.repository.GitRepoMemberRepository;
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

    @KafkaListener(topics = "gitrank.to.backend.commit", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (map.isEmpty()) {
            return;
        }
        String gitRepo = (String) map.get("gitRepo");
        List<GitRepoMemberResponse> list = new ArrayList<>();

        for (Object key :  map.keySet()) {
            Map<String, Integer> resultMap = (Map<String, Integer>)map.get(key);
            list.add(new GitRepoMemberResponse((String)key, resultMap.get("commits"), resultMap.get("addition"), resultMap.get("deletion")));
        }

        gitRepoMemberService.saveAllDto(list, gitRepo);
    }
}
