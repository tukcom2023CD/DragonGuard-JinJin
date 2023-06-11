package com.dragonguard.backend.domain.issue.service;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.mapper.IssueMapper;
import com.dragonguard.backend.domain.issue.repository.IssueRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

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

    public void saveIssues(final Member member, final Integer issueNum, final Integer year) {
        if (updateIssueNumIfNotExists(member, issueNum, year)) return;
        issueRepository.save(issueMapper.toEntity(member, issueNum, year));
    }

    private boolean updateIssueNumIfNotExists(final Member member, final Integer issueNum, final Integer year) {
        if (issueRepository.existsByMemberAndYear(member, year)) {
            findIssueAndUpdateNum(member, issueNum, year);
            return true;
        }
        return false;
    }

    public void findIssueAndUpdateNum(final Member member, final Integer issueNum, final Integer year) {
        Issue issue = issueRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
        issue.updateIssueNum(issueNum);
    }


    public List<Issue> findIssuesByMember(final Member member) {
        return issueRepository.findByMember(member);
    }

    @Override
    public Issue loadEntity(final Long id) {
        return issueRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
