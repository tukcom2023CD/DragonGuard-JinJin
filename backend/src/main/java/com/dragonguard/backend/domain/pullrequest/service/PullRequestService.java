package com.dragonguard.backend.domain.pullrequest.service;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.mapper.PullRequestMapper;
import com.dragonguard.backend.domain.pullrequest.repository.PullRequestRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@TransactionService
@RequiredArgsConstructor
public class PullRequestService implements EntityLoader<PullRequest, Long> {
    private final PullRequestRepository pullRequestRepository;
    private final PullRequestMapper pullRequestMapper;

    public void savePullRequests(final Member member, final Integer pullRequestNum, final Integer year) {
        if (pullRequestRepository.existsByMemberAndYear(member, year)) {
            PullRequest pullRequest = pullRequestRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
            pullRequest.updatePullRequestNum(pullRequestNum);
            return;
        }
        pullRequestRepository.save(pullRequestMapper.toEntity(member, pullRequestNum, year));
    }

    public List<PullRequest> findPullRequestByMember(final Member member) {
        return pullRequestRepository.findAllByMember(member);
    }

    @Override
    public PullRequest loadEntity(final Long id) {
        return pullRequestRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
