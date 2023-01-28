package com.dragonguard.backend.commit.messagequeue;

import com.dragonguard.backend.commit.dto.response.CommitScrappingResponse;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCommitConsumer {

    private final CommitService commitService;
    private final MemberService memberService;

    @KafkaListener(topics = "gitrank.to.backend.commit", containerFactory = "kafkaListenerContainerFactory")
    public void handle(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String githubId = (String) map.get("githubId");
        int commitNum = (Integer) map.get("commitNum");
        CommitScrappingResponse response = new CommitScrappingResponse(githubId, commitNum);
        commitService.saveCommit(response);
        memberService.addMemberCommit(githubId);
    }
}
