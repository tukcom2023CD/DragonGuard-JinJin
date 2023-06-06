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

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class KafkaRepositoryClientConsumer {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.repository.client", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<String, Object> map = null;

        try {
            map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {}

        if (Objects.isNull(map)) return;

        String githubId = (String) map.get("githubId");
        Member member = memberRepository.findByGithubId(githubId).orElseThrow(EntityNotFoundException::new);
        memberClientService.addMemberGitRepoAndGitOrganization(member);
    }
}
