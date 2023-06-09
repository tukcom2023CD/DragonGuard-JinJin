package com.dragonguard.backend.domain.gitrepo.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GitRepoKafkaResponse {
    private List<GitRepoMemberDetailsResponse> result;
}
