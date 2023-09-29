package com.dragonguard.backend.domain.issue.mapper;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

/**
 * @author 김승진
 * @description 깃허브 이슈 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = ComponentModel.SPRING)
public interface IssueMapper extends ContributionMapper<Issue> {}
