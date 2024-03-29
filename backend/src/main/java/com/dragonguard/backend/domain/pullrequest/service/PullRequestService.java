package com.dragonguard.backend.domain.pullrequest.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import com.dragonguard.backend.global.template.repository.ContributionRepository;
import com.dragonguard.backend.global.template.service.ContributionService;

/**
 * @author 김승진
 * @description PullRequest 관련 서비스 로직을 수행하는 클래스
 */
@TransactionService
public class PullRequestService extends ContributionService<PullRequest, Long> {

    public PullRequestService(
            final ContributionRepository<PullRequest, Long> contributionRepository,
            final ContributionMapper<PullRequest> commitMapper,
            final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer,
            final BlockchainService blockchainService) {
        super(contributionRepository, commitMapper, blockchainKafkaProducer, blockchainService);
    }
}
