package com.dragonguard.backend.domain.contribution.service;

import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
import com.dragonguard.backend.domain.contribution.mapper.ContributionMapper;
import com.dragonguard.backend.global.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final KafkaProducer<ContributionScrapingRequest> kafkaCommitProducer;
    private final ContributionMapper contributionMapper;

    public void scrapingCommits(String githubId) {
        kafkaCommitProducer.send(contributionMapper.toRequest(githubId));
    }
}
