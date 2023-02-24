package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoResponse {
    private GitRepoClientResponse gitRepo;
    private StatisticsResponse statistics;
    private Map<String, Integer> languages = new HashMap<>();
}
