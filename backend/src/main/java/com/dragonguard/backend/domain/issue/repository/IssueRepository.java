package com.dragonguard.backend.domain.issue.repository;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    List<Issue> findByMember(Member member);
    boolean existsByMemberAndYear(Member member, Integer year);
    Optional<Issue> findByMemberAndYear(Member member, Integer year);
    Issue save(Issue issue);
    Optional<Issue> findById(Long id);
}
