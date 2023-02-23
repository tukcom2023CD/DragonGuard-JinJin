package com.dragonguard.backend.gitrepomember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwoGitRepoMemberResponse {
    List<GitRepoMemberResponse> firstResult;
    List<GitRepoMemberResponse> secondResult;
}
