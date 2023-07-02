package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoKafkaResponse;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoMemberDetailsResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 kafka로 부터 받아와 처리하는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaGitRepoConsumer implements KafkaConsumer<GitRepoKafkaResponse> {
    private final GitRepoMemberService gitRepoMemberService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.git-repos", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message, Acknowledgment acknowledgment) {
        GitRepoKafkaResponse response = readValue(message);

        List<GitRepoMemberDetailsResponse> result = response.getResult();

        if (Objects.isNull(result) || result.isEmpty()) return;

        List<GitRepoMemberResponse> list = result.stream()
                .map(r -> GitRepoMemberResponse.builder().githubId(r.getMember()).commits(r.getCommits()).additions(r.getAddition()).deletions(r.getDeletion()).build())
                .collect(Collectors.toList());

        gitRepoMemberService.updateOrSaveAll(list, result.get(0).getGitRepo());
        acknowledgment.acknowledge();
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public GitRepoKafkaResponse readValue(String message) {
        return objectMapper.readValue(message, GitRepoKafkaResponse.class);
    }
}
