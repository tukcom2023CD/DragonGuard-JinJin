package com.dragonguard.backend.global.service;

import com.dragonguard.backend.domain.member.entity.Member;

/**
 * @author 김승진
 * @description 기여도 관련 서비스의 인터페이스
 */

public interface ContributionService<T, ID> extends EntityLoader<T, ID> {
    void saveContribution(final Member member, final Integer contributionNum, final Integer year);
}
