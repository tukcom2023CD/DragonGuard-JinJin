package com.dragonguard.backend.domain.gitrepo.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberDetailsResponse {
    private String gitRepo;
    private String member;
    private Integer commits;
    private Integer addition;
    private Integer deletion;
}
