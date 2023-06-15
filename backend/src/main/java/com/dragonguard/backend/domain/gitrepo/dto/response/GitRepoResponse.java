package com.dragonguard.backend.domain.gitrepo.dto.response;

import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GitRepoResponse {
    private List<Integer> sparkLine;
    private List<GitRepoMemberResponse> gitRepoMembers;
}
