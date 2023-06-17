package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberGitOrganizationResponse {
    private String name;
    private String profileImage;
}
