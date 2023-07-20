package com.dragonguard.backend.domain.gitrepo.dto.response;

import lombok.*;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 멤버 기여도 정보를 Github REST API에서 응답을 받아 담는 dto
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "githubId")
public class GitRepoMemberResponse {
    private String githubId;
    private String profileUrl;
    private Integer commits;
    private Integer additions;
    private Integer deletions;
    private Boolean isServiceMember;
}
