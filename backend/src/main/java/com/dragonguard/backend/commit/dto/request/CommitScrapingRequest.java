package com.dragonguard.backend.commit.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommitScrapingRequest {
    @NotEmpty
    String githubId;
    @NotNull
    Integer year;
}
