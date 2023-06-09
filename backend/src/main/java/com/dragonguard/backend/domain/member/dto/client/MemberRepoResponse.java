package com.dragonguard.backend.domain.member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description WebClient 응답에서 레포지토리 명을 가져오는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRepoResponse {
    private String full_name;
}
