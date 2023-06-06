package com.dragonguard.backend.domain.pullrequest.service;

import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.mapper.PullRequestMapper;
import com.dragonguard.backend.domain.pullrequest.repository.PullRequestRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@TransactionService
@RequiredArgsConstructor
public class PullRequestService implements EntityLoader<PullRequest, Long> {
    private final PullRequestRepository pullRequestRepository;
    private final PullRequestMapper pullRequestMapper;

    public void savePullRequests(final String githubId, final Integer pullRequestNum, final Integer year) {
        if (pullRequestRepository.existsByGithubIdAndYear(githubId, year)) {
            PullRequest pullRequest = pullRequestRepository.findByGithubIdAndYear(githubId, year).orElseThrow(EntityNotFoundException::new);
            pullRequest.updatePullRequestNum(pullRequestNum);
            return;
        }
        pullRequestRepository.save(pullRequestMapper.toEntity(githubId, pullRequestNum, year));
    }

    @Transactional(readOnly = true)
    public List<PullRequest> findPullRequestByGithubId(final String githubId) {
        return pullRequestRepository.findAllByGithubId(githubId);
    }

    @Override
    public PullRequest loadEntity(final Long id) {
        return pullRequestRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteAll(final List<PullRequest> pullRequests) {
        pullRequestRepository.deleteAll(pullRequests);
    }
}
