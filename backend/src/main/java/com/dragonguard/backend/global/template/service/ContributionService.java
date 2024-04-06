package com.dragonguard.backend.global.template.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.annotation.DistributedLock;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.entity.Contribution;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import com.dragonguard.backend.global.template.repository.ContributionRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 기여도 관련 서비스의 인터페이스
 */
@RequiredArgsConstructor
public abstract class ContributionService<T extends Contribution, ID>
        implements EntityLoader<T, ID> {
    private static final long NO_AMOUNT = 0L;
    private final ContributionRepository<T, ID> contributionRepository;
    private final ContributionMapper<T> commitMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @DistributedLock(name = "#member.getGithubId().concat(#contributeType.name())")
    public void saveContribution(
            final Member member,
            final Integer contributionNum,
            final Integer year,
            final ContributeType contributeType) {
        final Blockchain blockchain = blockchainService.findByType(member, contributeType);

        if (existsByMemberAndYear(member, year)) {
            updateAndSendTransaction(member, contributionNum, year, blockchain, contributeType);
            return;
        }
        contributionRepository.save(commitMapper.toEntity(member, contributionNum, year));
        sendTransaction(member, contributionNum.longValue(), blockchain, contributeType);
    }

    private void updateAndSendTransaction(
            final Member member,
            final Integer commitNum,
            final Integer year,
            final Blockchain blockchain,
            final ContributeType contributeType) {
        final T contribution = getContribution(member, year);
        if (isNotUpdatable(commitNum, contribution, commitNum - blockchain.getSumOfAmount())) {
            return;
        }
        contribution.updateContributionNum(commitNum);
        sendTransaction(member, commitNum.longValue(), blockchain, contributeType);
    }

    private boolean isNotUpdatable(
            final Integer commitNum, final T contribution, final long newBlockchainAmount) {
        return contribution.isNotUpdatable(commitNum) && newBlockchainAmount == NO_AMOUNT;
    }

    private T getContribution(final Member member, final Integer year) {
        return contributionRepository
                .findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return contributionRepository.existsByMemberAndYear(member, year);
    }

    private void sendTransaction(
            final Member member,
            final Long amount,
            final Blockchain blockchain,
            final ContributeType contributeType) {
        if (isInvalidToTransaction(member, amount, blockchain)) {
            return;
        }
        blockchainKafkaProducer.send(
                new BlockchainKafkaRequest(member.getId(), amount, contributeType));
    }

    private boolean isInvalidToTransaction(
            final Member member, final Long amount, final Blockchain blockchain) {
        return amount <= NO_AMOUNT
                || !member.isWalletAddressExists()
                || !blockchain.isNewHistory(amount);
    }

    public T loadEntity(final ID id) {
        return contributionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
