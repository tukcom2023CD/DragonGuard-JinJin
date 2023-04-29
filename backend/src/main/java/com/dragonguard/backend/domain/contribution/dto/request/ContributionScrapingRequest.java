package com.dragonguard.backend.domain.contribution.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author 김승진
 * @description 커밋 관련 요청 정보를 담는 dto
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionScrapingRequest {
    @NotEmpty
    private String githubId;
    @NotNull
    private Integer year;
}
