package com.dragonguard.backend.member.mapper;

import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.response.MemberDetailResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버 Entity와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class MemberMapper {
    public Member toEntity(MemberRequest memberRequest, AuthStep authStep) {
        return Member.builder()
                .githubId(memberRequest.getGithubId())
                .authStep(authStep)
                .build();
    }

    public Member toEntity(String githubId, Role role, AuthStep authStep) {
        return Member.builder()
                .githubId(githubId)
                .role(role)
                .authStep(authStep)
                .build();
    }

    public MemberResponse toResponse(Member member, Integer commits, Integer rank, Long amount, String organization, Integer organizationRank) {
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
                .organization(organization)
                .organizationRank(organizationRank)
                .build();
    }

    public MemberResponse toResponse(Member member, Integer commits, Integer issues, Integer pullRequests, Integer comments, Integer rank, Long amount) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .githubId(member.getGithubId())
                .commits(commits)
                .issues(issues)
                .pullRequests(pullRequests)
                .comments(comments)
                .tier(member.getTier())
                .authStep(member.getAuthStep())
                .profileImage(member.getProfileImage())
                .rank(rank)
                .tokenAmount(amount)
                .build();
    }

    public MemberDetailResponse toDetailResponse(MemberResponse dto, List<GitOrganization> gitOrganizations, List<GitRepo> gitRepos) {
        return MemberDetailResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .githubId(dto.getGithubId())
                .commits(dto.getCommits())
                .issues(dto.getIssues())
                .pullRequests(dto.getPullRequests())
                .comments(dto.getComments())
                .tier(dto.getTier())
                .authStep(dto.getAuthStep())
                .profileImage(dto.getProfileImage())
                .rank(dto.getRank())
                .tokenAmount(dto.getTokenAmount())
                .gitOrganizations(getGitOrganizationNames(gitOrganizations))
                .gitRepos(getGitRepoNames(gitRepos))
                .build();
    }

    private List<String> getGitOrganizationNames(List<GitOrganization> gitOrganizations) {
        return gitOrganizations.stream().map(GitOrganization::getName).collect(Collectors.toList());
    }

    private List<String> getGitRepoNames(List<GitRepo> gitRepos) {
        return gitRepos.stream().map(GitRepo::getName).collect(Collectors.toList());
    }
}
