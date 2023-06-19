package com.dragonguard.backend.domain.result.dto.response;

import lombok.*;

/**
 * @author 김승진
 * @description 레포지토리 검색 결과 응답 정보를 담는 dto
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoResultResponse {
    private Long id;
    private String name;
    private String language;
    private String description;
    private String createdAt;
}
