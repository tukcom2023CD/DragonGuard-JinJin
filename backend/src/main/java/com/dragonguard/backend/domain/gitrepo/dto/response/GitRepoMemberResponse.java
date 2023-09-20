package com.dragonguard.backend.domain.gitrepo.dto.response;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoMemberDetailsResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Member member;

    public GitRepoMemberResponse(final GitRepoMemberDetailsResponse response, final Member member) {
        this.githubId = response.getMember();
        this.commits = response.getCommits();
        this.additions = response.getAddition();
        this.deletions = response.getDeletion();
        this.member = member;
    }

    public GitRepoMemberResponse(
            final String githubId,
            final String profileUrl,
            final Integer commits,
            final Integer additions,
            final Integer deletions,
            final Boolean isServiceMember) {
        this.githubId = githubId;
        this.profileUrl = profileUrl;
        this.commits = commits;
        this.additions = additions;
        this.deletions = deletions;
        this.isServiceMember = isServiceMember;
    }
}
