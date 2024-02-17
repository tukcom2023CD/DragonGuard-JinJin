package com.dragonguard.backend.domain.member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description WebClient로의 요청시 필요한 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberClientRequest {
    private String githubId;
    private String githubToken;
    private Integer year;
}
