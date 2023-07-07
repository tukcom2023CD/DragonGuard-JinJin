package com.dragonguard.backend.domain.organization.dto.response;

import com.querydsl.core.Tuple;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버의 바로 위와 아래 랭킹을 보여주는 응답 dto 클래스
 */

@Getter
public class RelatedRankWithMemberResponse {
    private Integer organizationRank;
    private Boolean isLast;
    private List<String> memberGithubIds;

    public RelatedRankWithMemberResponse(Integer organizationRank, Boolean isLast, List<Tuple> memberGithubIds) {
        this.organizationRank = organizationRank;
        this.isLast = isLast;
        this.memberGithubIds = memberGithubIds.stream().map(t -> t.get(0, String.class)).collect(Collectors.toList());
    }
}
