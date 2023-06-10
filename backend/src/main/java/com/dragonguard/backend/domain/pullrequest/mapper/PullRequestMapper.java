package com.dragonguard.backend.domain.pullrequest.mapper;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author 김승진
 * @description 깃허브 Pull Request Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface PullRequestMapper {
    @Mapping(target = "amount", source = "pullRequestNum")
    PullRequest toEntity(final Member member, final Integer pullRequestNum, final Integer year);
}
