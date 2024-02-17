package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * @author 김승진
 * @description 멤버 상세 조회 응답 정보를 갖는 dto 클래스
 */
@Getter
@Builder
@AllArgsConstructor
public class MemberDetailsResponse {
    private Integer commits;
    private Integer issues;
    private Integer pullRequests;
    private Integer reviews;
    private String profileImage;
    private Set<String> gitRepos;
    private String organization;
    private Integer rank;
}
