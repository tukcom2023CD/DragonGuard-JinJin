package com.dragonguard.backend.domain.contribution.messagequeue;

import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
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
public class KafkaContributionProducer implements KafkaProducer<ContributionScrapingRequest> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(ContributionScrapingRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.contribution", "commit", request);
    }
}
