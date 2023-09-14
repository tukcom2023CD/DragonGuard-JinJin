package com.dragonguard.backend.domain.pullrequest.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.repository.PullRequestRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.mapper.ContributionEntityMapper;
import com.dragonguard.backend.global.service.ContributionService;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description PullRequest 관련 서비스 로직을 수행하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class PullRequestService implements ContributionService<PullRequest, Long> {
    private static final long NO_AMOUNT = 0L;
    private final PullRequestRepository pullRequestRepository;
    private final ContributionEntityMapper<PullRequest> pullRequestMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @Override
    public void saveContribution(final Member member, final Integer pullRequestNum, final Integer year) {
        final Blockchain blockchain = blockchainService.findBlockchainOfType(member, ContributeType.PULL_REQUEST);

        if (existsByMemberAndYear(member, year)) {
            updateAndSendTransaction(member, pullRequestNum, year, blockchain);
            return;
        }
        firstTransaction(member, pullRequestNum, year, blockchain);
    }

    private void updateAndSendTransaction(final Member member, final Integer pullRequestNum, final Integer year, final Blockchain blockchain) {
        final PullRequest pullRequest = getPullRequest(member, year);
        if (isNotUpdatable(pullRequestNum, pullRequest, pullRequestNum - blockchain.getSumOfAmount())) {
            return;
        }
        pullRequest.updatePullRequestNum(pullRequestNum);
        sendTransaction(member, pullRequestNum.longValue(), blockchain);
    }

    private boolean isNotUpdatable(final Integer pullRequestNum, final PullRequest pullRequest, final long newBlockchainAmount) {
        return pullRequest.isNotUpdatable(pullRequestNum) && newBlockchainAmount == NO_AMOUNT;
    }

    private void firstTransaction(final Member member, final Integer pullRequestNum, final Integer year, final Blockchain blockchain) {
        pullRequestRepository.save(pullRequestMapper.toEntity(member, pullRequestNum, year));
        sendTransaction(member, pullRequestNum.longValue(), blockchain);
    }

    private void sendTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        if (isInvalidToTransaction(member, amount, blockchain)) {
            return;
        }
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.PULL_REQUEST));
    }

    private boolean isInvalidToTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        return amount <= NO_AMOUNT || !member.isWalletAddressExists() || !blockchain.isNewHistory(amount);
    }

    private PullRequest getPullRequest(final Member member, final Integer year) {
        return pullRequestRepository.findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public PullRequest loadEntity(final Long id) {
        return pullRequestRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return pullRequestRepository.existsByMemberAndYear(member, year);
    }
}
