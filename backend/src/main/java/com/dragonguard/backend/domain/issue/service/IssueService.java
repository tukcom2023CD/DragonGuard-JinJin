package com.dragonguard.backend.domain.issue.service;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.mapper.IssueMapper;
import com.dragonguard.backend.domain.issue.repository.IssueRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 김승진
 * @description issue Entity의 서비스 로직을 담당하는 클래스
 */

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    @Transactional
    public void saveIssues(String githubId, Integer issueNum, Integer year) {
        if (issueRepository.existsByGithubIdAndYear(githubId, year)) {
            Issue issue = issueRepository.findByGithubIdAndYear(githubId, year).orElseThrow(EntityNotFoundException::new);
            issue.updateIssueNum(issueNum);
            return;
        }
        issueRepository.save(issueMapper.toEntity(githubId, issueNum, year));
    }

    public List<Issue> findIssuesByGithubId(String githubId) {
        return issueRepository.findByGithubId(githubId);
    }
}
