package com.dragonguard.backend.member.mapper;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member toEntity(MemberRequest memberRequest) {
        return Member.builder()
                .name(memberRequest.getName())
                .githubId(memberRequest.getGithubId())
                .build();
    }

    public MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .githubId(member.getGithubId())
                .tier(member.getTier())
                .authStep(member.getAuthStep())
                .build();
    }
}
