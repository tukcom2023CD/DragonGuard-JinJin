package com.dragonguard.backend.global.repository;

import com.dragonguard.backend.domain.member.entity.Member;

import java.util.Optional;

/**
 * @author 김승진
 * @description 기여도 관련 Repository를 묶는 인터페이스
 */

public interface ContributionRepository<T, ID> {
    T save(T contribution);
    Optional<T> findById(ID id);
    Optional<T> findByMemberAndYear(Member member, int year);
    boolean existsByMemberAndYear(Member member, int year);
}
