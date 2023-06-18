package com.dragonguard.backend.domain.result.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 김승진
 * @description 검색 결과를 Github REST API에서 받아오는 dto
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoClientResponse {
    private String full_name;
    private String language;
    private String description;
    private LocalDateTime created_at;
}
