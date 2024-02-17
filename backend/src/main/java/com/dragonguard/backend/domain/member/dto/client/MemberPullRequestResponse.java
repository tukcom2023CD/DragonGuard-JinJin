package com.dragonguard.backend.domain.member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description WebClient 응답에서 Pull Request 개수를 가져오는 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPullRequestResponse {
    private Integer totalCount;
}
