package com.dragonguard.backend.gitrepomember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberCompareRequest {
    private String firstName;
    private String firstRepo;
    private String secondName;
    private String secondRepo;
}
