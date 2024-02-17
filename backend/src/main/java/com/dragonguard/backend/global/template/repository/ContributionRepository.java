package com.dragonguard.backend.global.template.repository;

import com.dragonguard.backend.domain.member.entity.Member;

import java.util.Optional;

/**
 * @author 김승진
 * @description 기여도 관련 Repository를 묶는 인터페이스
 */
public interface ContributionRepository<T, ID> {
    T save(final T contribution);

    Optional<T> findById(final ID id);

    Optional<T> findByMemberAndYear(final Member member, final int year);

    boolean existsByMemberAndYear(final Member member, final int year);
}
