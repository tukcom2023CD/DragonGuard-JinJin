package com.dragonguard.backend.gitrepo.dto.response;

import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GirRepoMemberCompareResponse {
    private GitRepoMemberResponse firstMember;
    private GitRepoMemberResponse secondMember;
}
