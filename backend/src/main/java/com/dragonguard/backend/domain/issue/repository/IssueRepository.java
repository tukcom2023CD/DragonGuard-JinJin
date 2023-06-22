package com.dragonguard.backend.domain.issue.repository;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 이슈 관련 DB 접근을 처리하는 로직을 갖는 인터페이스
 */

public interface IssueRepository {
    List<Issue> findAllByMember(Member member);
    boolean existsByMemberAndYear(Member member, Integer year);
    Optional<Issue> findByMemberAndYear(Member member, Integer year);
    Issue save(Issue issue);
    Optional<Issue> findById(Long id);
}
