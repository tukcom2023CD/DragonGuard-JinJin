package com.dragonguard.backend.domain.issue.mapper;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.global.mapper.ContributionEntityMapper;
import org.mapstruct.Mapper;

/**
 * @author 김승진
 * @description 깃허브 이슈 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface IssueMapper extends ContributionEntityMapper<Issue> {}
