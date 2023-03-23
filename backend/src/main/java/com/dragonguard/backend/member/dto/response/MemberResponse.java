package com.dragonguard.backend.member.dto.response;

import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 응답 정보를 담는 dto
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private UUID id;
    private String name;
    private String githubId;
    private Integer commits;
    private Tier tier;
    private AuthStep authStep;
    private String profileImage;
    private Integer rank;
    private Long tokenAmount;
    private String organization;
}
