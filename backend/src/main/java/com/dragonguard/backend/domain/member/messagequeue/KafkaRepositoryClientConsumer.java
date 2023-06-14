package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.RepositoryClientResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberQueryRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaRepositoryClientConsumer implements KafkaConsumer<RepositoryClientResponse> {
    private final MemberClientService memberClientService;
    private final MemberQueryRepository memberQueryRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.repository.client", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        RepositoryClientResponse response = readValue(message);
        Member member = memberQueryRepository.findByGithubId(response.getGithubId())
                .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberGitRepoAndGitOrganization(member);
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public RepositoryClientResponse readValue(String message) {
        return objectMapper.readValue(message, RepositoryClientResponse.class);
    }
}
