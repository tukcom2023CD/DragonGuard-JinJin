package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaGitRepoInfoConsumer implements KafkaConsumer<GitRepoInfoRequest> {
    private final GitRepoService gitRepoService;
    private final ObjectMapper objectMapper;
    @Override
    public void consume(String message) {
        gitRepoService.requestToGithub(readValue(message));
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public GitRepoInfoRequest readValue(String message) {
        return objectMapper.readValue(message, GitRepoInfoRequest.class);
    }
}
