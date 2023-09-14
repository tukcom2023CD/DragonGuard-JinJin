package com.dragonguard.backend.domain.issue.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.repository.IssueRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.mapper.ContributionEntityMapper;
import com.dragonguard.backend.global.service.ContributionService;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description issue Entity의 서비스 로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class IssueService implements ContributionService<Issue, Long> {
    private static final long NO_AMOUNT = 0L;
    private final IssueRepository issueRepository;
    private final ContributionEntityMapper<Issue> issueMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @Override
    public void saveContribution(final Member member, final Integer issueNum, final Integer year) {
        final Blockchain blockchain = blockchainService.findBlockchainOfType(member, ContributeType.ISSUE);

        if (existsByMemberAndYear(member, year)) {
            updateAndSendTransaction(member, issueNum, year, blockchain);
            return;
        }
        firstTransaction(member, issueNum, year, blockchain);
    }

    private void updateAndSendTransaction(final Member member, final Integer issueNum, final Integer year, final Blockchain blockchain) {
        final Issue issue = getIssue(member, year);
        if (isNotUpdatable(issueNum, issue, issueNum - blockchain.getSumOfAmount())) {
            return;
        }
        issue.updateIssueNum(issueNum);
        sendTransaction(member, issueNum.longValue(), blockchain);
    }

    private boolean isNotUpdatable(final Integer issueNum, final Issue issue, final long newBlockchainAmount) {
        return issue.isNotUpdatable(issueNum) && newBlockchainAmount == NO_AMOUNT;
    }

    private void firstTransaction(final Member member, final Integer issueNum, final Integer year, final Blockchain blockchain) {
        issueRepository.save(issueMapper.toEntity(member, issueNum, year));
        sendTransaction(member, issueNum.longValue(), blockchain);
    }

    private Issue getIssue(final Member member, final Integer year) {
        return issueRepository.findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return issueRepository.existsByMemberAndYear(member, year);
    }

    private void sendTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        if (isInvalidToTransaction(member, amount, blockchain)) {
            return;
        }
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.ISSUE));
    }

    private boolean isInvalidToTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        return amount <= NO_AMOUNT || !member.isWalletAddressExists() || !blockchain.isNewHistory(amount);
    }

    @Override
    public Issue loadEntity(final Long id) {
        return issueRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
