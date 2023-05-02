package com.dragonguard.backend.domain.contribution.messagequeue;

import com.dragonguard.backend.domain.member.service.MemberService;
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
public class KafkaContributionConsumer {

    private final MemberService memberService;

    @KafkaListener(topics = "gitrank.to.backend.contribution", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (map.isEmpty()) {
            return;
        }

        String githubId = (String) map.get("githubId");
        String name = (String) map.get("name");
        int contribution = (Integer) map.get("contribution");
        String profileImage = (String) map.get("profileImage");

        memberService.addMemberCommitAndUpdate(githubId, name, profileImage, contribution);
    }
}
