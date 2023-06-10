package com.dragonguard.backend.domain.commit.mapper;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 커밋내역 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface CommitMapper {
    @Mapping(target = "year", expression = "java(LocalDate.now().getYear())")
    @Mapping(target = "amount", source = "contributionScrapingResponse.commitNum")
    Commit toEntity(final ContributionScrapingResponse contributionScrapingResponse, Member member);
}
