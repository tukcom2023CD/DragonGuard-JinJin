package com.dragonguard.backend.domain.pullrequest.service;

import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.repository.PullRequestRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.domain.pullrequest.mapper.PullRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PullRequestService {
    private final PullRequestRepository pullRequestRepository;
    private final PullRequestMapper pullRequestMapper;

    @Transactional
    public void savePullRequests(String githubId, Integer pullRequestNum, Integer year) {
        if (pullRequestRepository.existsByGithubIdAndYear(githubId, year)) {
            PullRequest pullRequest = pullRequestRepository.findByGithubIdAndYear(githubId, year).orElseThrow(EntityNotFoundException::new);
            pullRequest.updatePullRequestNum(pullRequestNum);
            return;
        }
        pullRequestRepository.save(pullRequestMapper.toEntity(githubId, pullRequestNum, year));
    }

    public List<PullRequest> findPullrequestByGithubId(String githubId) {
        return pullRequestRepository.findByGithubId(githubId);
    }
}
