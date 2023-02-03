package com.dragonguard.backend.gitrepomember.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberResponse {
    private String memberName;
    private Integer commits;
    private Integer additions;
    private Integer deletions;
}
