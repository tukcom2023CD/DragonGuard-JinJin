package com.dragonguard.backend.global.template.mapper;

import com.dragonguard.backend.domain.member.entity.Member;

/**
 * @author 김승진
 * @description 기여도 관련 서비스의 mapper 인터페이스
 */
public interface ContributionMapper<T> {
    T toEntity(final Member member, final Integer amount, final Integer year);
}
