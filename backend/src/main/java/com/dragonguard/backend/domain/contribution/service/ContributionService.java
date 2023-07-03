package com.dragonguard.backend.domain.contribution.service;

import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
import com.dragonguard.backend.domain.contribution.mapper.ContributionMapper;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 김승진
 * @description 멤버의 기여도에 대한 서비스 로직을 처리하는 클래스
 */

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final KafkaProducer<ContributionScrapingRequest> kafkaCommitProducer;
    private final ContributionMapper contributionMapper;

    public void scrapingContributions(final String githubId) {
        kafkaCommitProducer.send(contributionMapper.toRequest(githubId));
    }
}
