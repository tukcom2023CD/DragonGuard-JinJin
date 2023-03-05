package com.dragonguard.backend.commit.messagequeue;

import com.dragonguard.backend.commit.dto.request.CommitScrapingRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 커밋 정보를 스크래핑 모듈로 요청하기 위해 카프카로 보내는 producer
 */

@Component
@RequiredArgsConstructor
public class KafkaCommitProducer implements KafkaProducer<CommitScrapingRequest> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(CommitScrapingRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.commit", "commit", request);
    }
}
