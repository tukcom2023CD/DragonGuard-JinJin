package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberGitOrganizationRepoResponse {
    private String profileImage;
    private List<String> gitRepos;
}
