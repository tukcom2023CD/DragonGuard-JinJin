package com.dragonguard.backend.domain.gitrepomember.dto.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 멤버 세부 정보를 Github REST API에서 응답을 받아 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Author {
    private String login;
    private String avatarUrl;
}
