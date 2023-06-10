package com.dragonguard.backend.domain.contribution.mapper;

import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 기여정보 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface ContributionMapper {
    @Mapping(target = "year", expression = "java(LocalDate.now().getYear())")
    ContributionScrapingRequest toRequest(final String githubId);
}
