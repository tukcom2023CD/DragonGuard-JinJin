package com.dragonguard.backend.commit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommitScrappingResponse {
    String githubId;
    Integer commitNum;
}
