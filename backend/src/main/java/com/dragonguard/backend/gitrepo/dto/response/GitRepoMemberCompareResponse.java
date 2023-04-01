package com.dragonguard.backend.gitrepo.dto.response;

import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 김승진
 * @description 깃허브 Repository 멤버 비교 정보를 담는 응답 dto
 */

@Getter
@ToString
@AllArgsConstructor
public class GitRepoMemberCompareResponse {
    private GitRepoMemberResponse firstMember;
    private GitRepoMemberResponse secondMember;
}
