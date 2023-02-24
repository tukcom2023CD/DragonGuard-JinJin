package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoResponse {
    private GitRepoClientResponse gitRepo;
    private StatisticsResponse statistics;
}
