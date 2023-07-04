package com.dragonguard.backend.domain.pullrequest.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
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
        if (isExistsByMemberAndYear(member, year)) {
            Integer newAmount = updatePullRequestNum(member, pullRequestNum, year);
            sendTransaction(member, newAmount.longValue());
            return;
        }
        pullRequestRepository.save(pullRequestMapper.toEntity(member, pullRequestNum, year));
        sendTransaction(member, pullRequestNum.longValue());
    }

    private void sendTransaction(Member member, Long amount) {
        if (amount <= 0 || !member.isWalletAddressExists()) return;
        Long blockchainId = blockchainService.getBlockchainOfType(member, ContributeType.CODE_REVIEW).getId();
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.CODE_REVIEW, blockchainId));
    }

    private Integer updatePullRequestNum(final Member member, final Integer pullRequestNum, final Integer year) {
        PullRequest pullRequest = pullRequestRepository.findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
        Integer newAmount = pullRequestNum - pullRequest.getAmount();
        pullRequest.updatePullRequestNum(pullRequestNum);
        return newAmount;
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
