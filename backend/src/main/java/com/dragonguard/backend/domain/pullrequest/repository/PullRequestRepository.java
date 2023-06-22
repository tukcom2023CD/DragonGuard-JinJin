package com.dragonguard.backend.domain.pullrequest.repository;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description Pull Request에 대한 DB 접근 로직을 갖는 인터페이스
 */

public interface PullRequestRepository {
    boolean existsByMemberAndYear(Member member, Integer year);
    Optional<PullRequest> findByMemberAndYear(Member member, Integer year);
    List<PullRequest> findAllByMember(Member member);
    PullRequest save(PullRequest pullRequest);
    Optional<PullRequest> findById(Long id);
}
