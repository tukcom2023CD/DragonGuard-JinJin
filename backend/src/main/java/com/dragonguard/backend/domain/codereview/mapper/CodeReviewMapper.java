package com.dragonguard.backend.domain.codereview.mapper;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.member.entity.Member;
import org.mapstruct.Mapper;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 코드리뷰 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface CodeReviewMapper {
    CodeReview toEntity(final Integer amount, final Integer year, final Member member);
}
