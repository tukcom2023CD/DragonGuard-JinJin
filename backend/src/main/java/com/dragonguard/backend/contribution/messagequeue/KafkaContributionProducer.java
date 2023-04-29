package com.dragonguard.backend.contribution.messagequeue;

import com.dragonguard.backend.contribution.dto.request.CommitScrapingRequest;
import com.dragonguard.backend.util.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 커밋 정보를 스크래핑 모듈로 요청하기 위해 카프카로 보내는 producer
 */

@Component
@RequiredArgsConstructor
public class KafkaContributionProducer implements KafkaProducer<CommitScrapingRequest> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(CommitScrapingRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.contribution", "commit", request);
    }
}
