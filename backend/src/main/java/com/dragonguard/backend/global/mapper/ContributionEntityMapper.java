package com.dragonguard.backend.global.mapper;

import com.dragonguard.backend.domain.member.entity.Member;

/**
 * @author 김승진
 * @description 기여도 관련 서비스의 mapper 인터페이스
 */

public interface ContributionEntityMapper<T> extends EntityMapper {
    T toEntity(final Member member, final Integer amount, final Integer year);
}
