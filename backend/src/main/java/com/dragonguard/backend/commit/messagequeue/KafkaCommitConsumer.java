package com.dragonguard.backend.commit.messagequeue;

import com.dragonguard.backend.commit.dto.response.CommitScrapingResponse;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 김승진
 * @description 커밋 정보를 카프카로부터 받아오는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaCommitConsumer {

    private final CommitService commitService;
    private final MemberService memberService;

    @KafkaListener(topics = "gitrank.to.backend.commit", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (map.isEmpty()) {
            return;
        }

        String githubId = (String) map.get("githubId");
        String name = (String) map.get("name");
        int commitNum = (Integer) map.get("commitNum");
        String profileImage = (String) map.get("profileImage");

        CommitScrapingResponse response = new CommitScrapingResponse(githubId, commitNum);
        commitService.saveCommit(response);
        memberService.addMemberCommitAndUpdate(githubId, name, profileImage);
    }
}
