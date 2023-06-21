package com.dragonguard.backend.domain.contribution.service;

import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
import com.dragonguard.backend.domain.contribution.mapper.ContributionMapper;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

@TransactionService
@RequiredArgsConstructor
public class ContributionService {
    private final KafkaProducer<ContributionScrapingRequest> kafkaCommitProducer;
    private final ContributionMapper contributionMapper;

    public void scrapingCommits(final String githubId) {
        kafkaCommitProducer.send(contributionMapper.toRequest(githubId));
    }
}
