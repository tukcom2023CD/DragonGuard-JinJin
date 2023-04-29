package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.mapper.CommitMapper;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommitService {

    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;

    public void saveCommits(ContributionScrapingResponse contributionScrapingResponse) {
        List<Commit> commits
                = commitRepository.findCommitsByGithubId(contributionScrapingResponse.getGithubId());
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

    public List<Commit> findCommits(String githubId) {
        return commitRepository.findCommitsByGithubId(githubId);
    }
}
