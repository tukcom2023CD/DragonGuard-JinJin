package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwoGitRepoResponse {
    private GitRepoResponse firstRepo;
    private GitRepoResponse secondRepo;
}
