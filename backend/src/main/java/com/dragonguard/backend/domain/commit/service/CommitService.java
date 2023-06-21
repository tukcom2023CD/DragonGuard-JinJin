package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.mapper.CommitMapper;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.EntityLoader;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 김승진
 * @description 커밋과 관련된 서비스 로직을 처리하는 클래스
 */

@Service
@RequiredArgsConstructor
public class CommitService implements EntityLoader<Commit, Long> {
    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;

    public void saveCommits(final ContributionScrapingResponse contributionScrapingResponse, final Member member) {
        List<Commit> commits = commitRepository.findAllByMember(member);
        Commit commit = commitMapper.toEntity(contributionScrapingResponse, member);

        if (commits.isEmpty()) {
            commitRepository.save(commit);
            return;
        }

        saveIfIsNewCommitWithSameMember(commits, commit);
    }

    public List<Commit> findCommitsByMember(final Member member) {
        return commitRepository.findAllByMember(member);
    }

    @Override
    public Commit loadEntity(final Long id) {
        return commitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void saveIfIsNewCommitWithSameMember(final List<Commit> commits, final Commit commit) {
        commits.stream()
                .filter(c -> isNewCommitWithSameMember(commit, c))
                .findFirst()
                .ifPresent(commitRepository::save);
    }

    public boolean isNewCommitWithSameMember(final Commit compareCommit, final Commit commit) {
        return !commit.customEqualsWithAmount(compareCommit);
    }
}
