package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.mapper.CommitMapper;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@TransactionService
@RequiredArgsConstructor
public class CommitService implements EntityLoader<Commit, Long> {

    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;

    public void saveCommits(final ContributionScrapingResponse contributionScrapingResponse) {
        List<Commit> commits
                = commitRepository.findAllByGithubId(contributionScrapingResponse.getGithubId());
        Commit commit = commitMapper.toEntity(contributionScrapingResponse);

        if (commits.isEmpty()) {
            commitRepository.save(commit);
            return;
        }
        commits.stream()
                .filter(c -> !c.equals(commit))
                .findFirst()
                .ifPresent(commitRepository::save);
    }

    @Transactional(readOnly = true)
    public List<Commit> findCommits(final String githubId) {
        return commitRepository.findAllByGithubId(githubId);
    }

    @Override
    public Commit loadEntity(final Long id) {
        return commitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
