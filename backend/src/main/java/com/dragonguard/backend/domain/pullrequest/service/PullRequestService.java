package com.dragonguard.backend.domain.pullrequest.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.mapper.PullRequestMapper;
import com.dragonguard.backend.domain.pullrequest.repository.PullRequestRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description PullRequest 관련 서비스 로직을 수행하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class PullRequestService implements EntityLoader<PullRequest, Long> {
    private final PullRequestRepository pullRequestRepository;
    private final PullRequestMapper pullRequestMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    public void savePullRequests(final Member member, final Integer pullRequestNum, final Integer year) {
        Blockchain blockchain = blockchainService.getBlockchainOfType(member, ContributeType.PULL_REQUEST);

        if (isExistsByMemberAndYear(member, year)) {
            updatePullRequestNum(member, pullRequestNum, year);
            sendTransaction(member, pullRequestNum - blockchain.getSumOfAmount(), blockchain.getId());
            return;
        }
        pullRequestRepository.save(pullRequestMapper.toEntity(member, pullRequestNum, year));
        sendTransaction(member, pullRequestNum.longValue(), blockchain.getId());
    }

    private void sendTransaction(Member member, Long amount, Long blockchainId) {
        if (amount <= 0 || !member.isWalletAddressExists()) return;
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.PULL_REQUEST, blockchainId));
    }

    private void updatePullRequestNum(final Member member, final Integer pullRequestNum, final Integer year) {
        PullRequest pullRequest = pullRequestRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
        pullRequest.updatePullRequestNum(pullRequestNum);
    }

    @Override
    public PullRequest loadEntity(final Long id) {
        return pullRequestRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean isExistsByMemberAndYear(final Member member, final Integer year) {
        return pullRequestRepository.existsByMemberAndYear(member, year);
    }
}
