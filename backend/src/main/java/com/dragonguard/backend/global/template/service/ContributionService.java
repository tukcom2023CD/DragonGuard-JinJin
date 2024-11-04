package com.dragonguard.backend.global.template.service;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.annotation.DistributedLock;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.entity.Contribution;
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
    private final ContributionMapper<T> contributionMapper;

    @DistributedLock(name = "#member.getGithubId().concat(#contributeType.name())")
    public void saveContribution(
            final Member member,
            final Integer contributionNum,
            final Integer year) {
        if (existsByMemberAndYear(member, year)) {
            updateAndSendTransaction(member, contributionNum, year);
            return;
        }
        contributionRepository.save(contributionMapper.toEntity(member, contributionNum, year));
    }

    private void updateAndSendTransaction(
            final Member member, final Integer commitNum, final Integer year) {
        final T contribution = getContribution(member, year);
        if (isNotUpdatable(commitNum, contribution)) {
            return;
        }
        contribution.updateContributionNum(commitNum);
    }

    private boolean isNotUpdatable(final Integer commitNum, final T contribution) {
        return contribution.isNotUpdatable(commitNum);
    }

    private T getContribution(final Member member, final Integer year) {
        return contributionRepository
                .findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return contributionRepository.existsByMemberAndYear(member, year);
    }

    public T loadEntity(final ID id) {
        return contributionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
