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
import com.dragonguard.backend.global.mapper.ContributionEntityMapper;
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
    private static final long NONE = 0L;
    private final CommitRepository commitRepository;
    private final ContributionEntityMapper<Commit> commitMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @Override
    public void saveContribution(final Member member, final Integer commitNum, final Integer year) {
        Blockchain blockchain = blockchainService.findBlockchainOfType(member, ContributeType.COMMIT);

        if (existsByMemberAndYear(member, year)) {
            Commit commit = getCommit(member, year);
            long newBlockchainAmount = commitNum - blockchain.getSumOfAmount();
            if (commit.isNotUpdatable(commitNum) && newBlockchainAmount == NONE) return;
            commit.updateCommitNum(commitNum);
            sendTransaction(member, commitNum.longValue(), blockchain);
            return;
        }
        commitRepository.save(commitMapper.toEntity(member, commitNum, year));
        sendTransaction(member, commitNum.longValue(), blockchain);
    }

    private Commit getCommit(final Member member, final Integer year) {
        return commitRepository.findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return commitRepository.existsByMemberAndYear(member, year);
    }

    private void sendTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        if (amount <= 0 || !member.isWalletAddressExists() || !blockchain.isNewHistory(amount)) return;
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.COMMIT));
    }

    @Override
    public Commit loadEntity(final Long id) {
        return commitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
