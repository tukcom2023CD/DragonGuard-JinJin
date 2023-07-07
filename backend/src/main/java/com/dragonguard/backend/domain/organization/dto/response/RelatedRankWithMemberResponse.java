package com.dragonguard.backend.domain.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author 김승진
 * @description 멤버의 바로 위와 아래 랭킹을 보여주는 응답 dto 클래스
 */

@Getter
@AllArgsConstructor
public class RelatedRankWithMemberResponse {
    private Integer organizationRank;
    private Boolean isLast;
    private List<String> memberGithubIds;
}
