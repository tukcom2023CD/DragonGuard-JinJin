package com.dragonguard.backend.domain.search.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoResultResponses {
    private List<GitRepoResultResponse> data;
}
