package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.mapper.CommitMapper;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@TransactionService
@RequiredArgsConstructor
public class CommitService implements EntityLoader<Commit, Long> {

    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;

    public void saveCommits(final ContributionScrapingResponse contributionScrapingResponse, Member member) {
        List<Commit> commits
                = commitRepository.findAllByMember(member);
        Commit commit = commitMapper.toEntity(contributionScrapingResponse, member);

        if (commits.isEmpty()) {
            commitRepository.save(commit);
            return;
        }
        commits.stream()
                .filter(c -> !c.customEqualsWithAmount(commit))
                .findFirst()
                .ifPresent(commitRepository::save);
    }

    public List<Commit> findCommitsByMember(final Member member) {
        return commitRepository.findAllByMember(member);
    }

    @Override
    public Commit loadEntity(final Long id) {
        return commitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
