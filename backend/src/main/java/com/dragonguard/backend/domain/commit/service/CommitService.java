package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.mapper.CommitMapper;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
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

    public void saveCommits(final Member member, final Integer commitNum, final Integer year) {
        if (commitRepository.existsByMemberAndYear(member, year)) {
            findCommitAndUpdateNum(member, commitNum, year);
            return;
        }
        commitRepository.save(commitMapper.toEntity(commitNum, year, member));
    }

    public void findCommitAndUpdateNum(final Member member, final Integer commitNum, final Integer year) {
        Commit commit = commitRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
        commit.updateCommitNum(commitNum);
    }

    @Override
    public Commit loadEntity(final Long id) {
        return commitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
