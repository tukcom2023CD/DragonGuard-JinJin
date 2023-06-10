package com.dragonguard.backend.domain.member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description WebClient 응답에서 조직명을 가져오는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRepoResponse {
    private String full_name;
}
