package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.mapper.ContributionMapper;
import com.dragonguard.backend.global.service.ContributionService;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 커밋과 관련된 서비스 로직을 처리하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class CommitService implements ContributionService<Commit, Long> {
    private static final long NO_AMOUNT = 0L;
    private final CommitRepository commitRepository;
    private final ContributionMapper<Commit> commitMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @Override
    public void saveContribution(final Member member, final Integer commitNum, final Integer year) {
        final Blockchain blockchain = blockchainService.findBlockchainOfType(member, ContributeType.COMMIT);

        if (existsByMemberAndYear(member, year)) {
            updateAndSendTransaction(member, commitNum, year, blockchain);
            return;
        }
        commitRepository.save(commitMapper.toEntity(member, commitNum, year));
        sendTransaction(member, commitNum.longValue(), blockchain);
    }

    private void updateAndSendTransaction(final Member member, final Integer commitNum, final Integer year, final Blockchain blockchain) {
        final Commit commit = getCommit(member, year);
        if (isNotUpdatable(commitNum, commit, commitNum - blockchain.getSumOfAmount())) {
            return;
        }
        commit.updateCommitNum(commitNum);
        sendTransaction(member, commitNum.longValue(), blockchain);
    }

    private boolean isNotUpdatable(final Integer commitNum, final Commit commit, final long newBlockchainAmount) {
        return commit.isNotUpdatable(commitNum) && newBlockchainAmount == NO_AMOUNT;
    }

    private Commit getCommit(final Member member, final Integer year) {
        return commitRepository.findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return commitRepository.existsByMemberAndYear(member, year);
    }

    private void sendTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        if (isInvalidToTransaction(member, amount, blockchain)) {
            return;
        }
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.COMMIT));
    }

    private boolean isInvalidToTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        return amount <= NO_AMOUNT || !member.isWalletAddressExists() || !blockchain.isNewHistory(amount);
    }

    @Override
    public Commit loadEntity(final Long id) {
        return commitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
