package com.dragonguard.backend.member.dto.response;

import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String githubId;
    private Integer commits;
    private Tier tier;
    private AuthStep authStep;
    private String profileImage;
}
