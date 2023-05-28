package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaRepositoryClientConsumer {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;

    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.repository.client", containerFactory = "kafkaListenerContainerFactory")
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
        Member member = memberRepository.findMemberByGithubId(githubId).orElseThrow(EntityNotFoundException::new);
        memberClientService.addMemberGitRepoAndGitOrganization(member);
    }
}
