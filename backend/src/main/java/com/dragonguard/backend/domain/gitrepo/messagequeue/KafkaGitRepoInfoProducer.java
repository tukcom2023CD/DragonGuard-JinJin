package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoRequest;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 레포지토리 kafka 요청을 보내는 consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaGitRepoInfoProducer implements KafkaProducer<GitRepoRequest> {
    private static final String TOPIC = "gitrank.to.backend.git-repos-info";
    private static final String KEY = "git-repos-info";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public void send(final GitRepoRequest request) {
        kafkaTemplate.send(TOPIC, KEY, request);
    }
}
