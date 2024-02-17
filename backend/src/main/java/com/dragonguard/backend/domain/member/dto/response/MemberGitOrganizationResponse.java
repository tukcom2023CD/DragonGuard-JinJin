package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author 김승진
 * @description 멤버 깃허브 조직의 레포지토리 응답 정보를 갖는 dto 클래스
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "profileImage"})
public class MemberGitOrganizationResponse {
    private String name;
    private String profileImage;
}
