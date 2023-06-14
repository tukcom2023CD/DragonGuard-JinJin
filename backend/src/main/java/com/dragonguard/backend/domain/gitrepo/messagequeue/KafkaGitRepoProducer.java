package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 스크래핑하기 위해 kafka로 요청하는 Producer
 */

@Component
@RequiredArgsConstructor
public class KafkaGitRepoProducer implements KafkaProducer<GitRepoRequest> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(GitRepoRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.git-repos", "git-repos", request);
    }
}
