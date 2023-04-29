package com.dragonguard.backend.domain.contribution.service;

import com.dragonguard.backend.domain.contribution.dto.request.CommitScrapingRequest;
import com.dragonguard.backend.domain.contribution.mapper.ContributionMapper;
import com.dragonguard.backend.util.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final KafkaProducer<CommitScrapingRequest> kafkaCommitProducer;
    private final ContributionMapper contributionMapper;

    public void scrapingCommits(String githubId) {
        kafkaCommitProducer.send(contributionMapper.toRequest(githubId));
    }
}
