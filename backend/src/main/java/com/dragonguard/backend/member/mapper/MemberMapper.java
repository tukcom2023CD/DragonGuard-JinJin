package com.dragonguard.backend.member.mapper;

import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member toEntity(MemberRequest memberRequest) {
        return Member.builder()
                .githubId(memberRequest.getGithubId())
                .build();
    }

    public MemberResponse toResponse(Member member, Integer commits, Integer rank, Long amount) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .githubId(member.getGithubId())
                .commits(commits)
                .tier(member.getTier())
                .authStep(member.getAuthStep())
                .profileImage(member.getProfileImage())
                .rank(rank)
                .tokenAmount(amount)
                .build();
    }
}
