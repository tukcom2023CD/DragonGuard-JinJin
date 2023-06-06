package com.dragonguard.backend.domain.issue.service;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.mapper.IssueMapper;
import com.dragonguard.backend.domain.issue.repository.IssueRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 김승진
 * @description issue Entity의 서비스 로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class IssueService implements EntityLoader<Issue, Long> {
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    public void saveIssues(final String githubId, final Integer issueNum, final Integer year) {
        if (issueRepository.existsByGithubIdAndYear(githubId, year)) {
            Issue issue = issueRepository.findByGithubIdAndYear(githubId, year).orElseThrow(EntityNotFoundException::new);
            issue.updateIssueNum(issueNum);
            return;
        }
        issueRepository.save(issueMapper.toEntity(githubId, issueNum, year));
    }


    @Transactional(readOnly = true)
    public List<Issue> findIssuesByGithubId(final String githubId) {
        return issueRepository.findByGithubId(githubId);
    }

    @Override
    public Issue loadEntity(final Long id) {
        return issueRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteAll(final List<Issue> issues) {
        issueRepository.deleteAll(issues);
    }
}
