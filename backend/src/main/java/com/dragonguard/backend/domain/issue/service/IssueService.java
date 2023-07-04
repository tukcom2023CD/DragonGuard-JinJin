package com.dragonguard.backend.domain.issue.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.mapper.IssueMapper;
import com.dragonguard.backend.domain.issue.repository.IssueRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description issue Entity의 서비스 로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class IssueService implements EntityLoader<Issue, Long> {
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    public void saveIssues(final Member member, final Integer issueNum, final Integer year) {
        if (issueRepository.existsByMemberAndYear(member, year)) {
            Integer newAmount = findIssueAndUpdateNum(member, issueNum, year);
            sendTransaction(member, newAmount.longValue());
            return;
        }
        issueRepository.save(issueMapper.toEntity(member, issueNum, year));
        sendTransaction(member, issueNum.longValue());
    }

    private void sendTransaction(Member member, Long amount) {
        if (amount <= 0 || !member.isWalletAddressExists()) return;
        Long blockchainId = blockchainService.getBlockchainOfType(member, ContributeType.CODE_REVIEW).getId();
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.CODE_REVIEW, blockchainId));
    }

    private Integer findIssueAndUpdateNum(final Member member, final Integer issueNum, final Integer year) {
        Issue issue = issueRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
        Integer newAmount = issueNum - issue.getAmount();
        issue.updateIssueNum(issueNum);
        return newAmount;
    }

    @Override
    public Issue loadEntity(final Long id) {
        return issueRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
