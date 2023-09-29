package com.dragonguard.backend.domain.codereview.mapper;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 코드리뷰 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = ComponentModel.SPRING, imports = {LocalDate.class})
public interface CodeReviewMapper extends ContributionMapper<CodeReview> {}
