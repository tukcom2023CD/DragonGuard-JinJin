package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.mapper.CommitMapper;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 커밋과 관련된 서비스 로직을 처리하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class CommitService implements EntityLoader<Commit, Long> {
    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    public void saveCommits(final Member member, final Integer commitNum, final Integer year) {
        if (commitRepository.existsByMemberAndYear(member, year)) {
            Integer newAmount = findCommitAndUpdateNum(member, commitNum, year);
            sendTransaction(member, newAmount.longValue());
            return;
        }
        commitRepository.save(commitMapper.toEntity(commitNum, year, member));
        sendTransaction(member, commitNum.longValue());
    }

    private void sendTransaction(Member member, Long amount) {
        if (amount <= 0 || !member.isWalletAddressExists()) return;
        Long blockchainId = blockchainService.getBlockchainOfType(member, ContributeType.CODE_REVIEW).getId();
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.CODE_REVIEW, blockchainId));
    }

    private Integer findCommitAndUpdateNum(final Member member, final Integer commitNum, final Integer year) {
        Commit commit = commitRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
        Integer newAmount = commitNum - commit.getAmount();
        commit.updateCommitNum(commitNum);
        return newAmount;
    }

    @Override
    public Commit loadEntity(final Long id) {
        return commitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
