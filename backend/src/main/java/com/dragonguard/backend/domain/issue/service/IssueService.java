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
    private final IssueRepository issueRepository;
    private final ContributionEntityMapper<Issue> issueMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @Override
    public void saveContribution(final Member member, final Integer issueNum, final Integer year) {
        Blockchain blockchain = blockchainService.getBlockchainOfType(member, ContributeType.ISSUE);

        if (existsByMemberAndYear(member, year)) {
            Issue issue = findIssue(member, year);
            if (issue.isOldContribution(issueNum)) return;
            issue.updateIssueNum(issueNum);
            sendTransaction(member, issueNum - blockchain.getSumOfAmount(), blockchain);
            return;
        }
        issueRepository.save(issueMapper.toEntity(member, issueNum, year));
        sendTransaction(member, issueNum.longValue(), blockchain);
    }

    private Issue findIssue(final Member member, final Integer year) {
        return issueRepository.findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return issueRepository.existsByMemberAndYear(member, year);
    }

    private void sendTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        if (amount <= 0 || !member.isWalletAddressExists() || !blockchain.isNewHistory(amount)) return;
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.ISSUE));
    }

    @Override
    public Issue loadEntity(final Long id) {
        return issueRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
