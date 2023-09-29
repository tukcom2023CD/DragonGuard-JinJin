package com.dragonguard.backend.domain.pullrequest.mapper;

import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

/**
 * @author 김승진
 * @description 깃허브 Pull Request Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = ComponentModel.SPRING)
public interface PullRequestMapper extends ContributionMapper<PullRequest> {}
