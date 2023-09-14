package com.dragonguard.backend.global.repository;

import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * @author 김승진
 * @description 기여도 관련 Repository를 묶는 인터페이스
 */

@NoRepositoryBean
public interface ContributionRepository<T, ID> extends EntityRepository<T, ID> {
    Optional<T> findById(final ID id);
    Optional<T> findByMemberAndYear(final Member member, final int year);
    boolean existsByMemberAndYear(final Member member, final int year);
}
