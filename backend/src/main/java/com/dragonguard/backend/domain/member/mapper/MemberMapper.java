package com.dragonguard.backend.domain.member.mapper;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.dto.response.MemberDetailResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    public MemberResponse toResponse(Member member, Integer rank, Long amount, String organization, Integer organizationRank) {
        Optional<Integer> sumOfCommits = member.getSumOfCommits();
        Optional<Integer> sumOfIssues = member.getSumOfIssues();
        Optional<Integer> sumOfPullRequests = member.getSumOfPullRequests();
        Optional<Integer> sumOfReviews = member.getSumOfReviews();

        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .githubId(member.getGithubId())
                .commits(sumOfCommits.orElse(null))
                .issues(sumOfIssues.orElse(null))
                .pullRequests(sumOfPullRequests.orElse(null))
                .reviews(sumOfReviews.orElse(null))
                .tier(member.getTier())
                .authStep(member.getAuthStep())
                .profileImage(member.getProfileImage())
                .rank(rank)
                .tokenAmount(amount)
                .organization(organization)
                .organizationRank(organizationRank)
                .blockchainUrl(member.getBlockchainUrl())
                .build();
    }

    public MemberResponse toResponse(Member member, Integer rank, Long amount) {
        Optional<Integer> sumOfCommits = member.getSumOfCommits();
        Optional<Integer> sumOfIssues = member.getSumOfIssues();
        Optional<Integer> sumOfPullRequests = member.getSumOfPullRequests();
        Optional<Integer> sumOfReviews = member.getSumOfReviews();

        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .githubId(member.getGithubId())
                .commits(sumOfCommits.orElse(null))
                .issues(sumOfIssues.orElse(null))
                .pullRequests(sumOfPullRequests.orElse(null))
                .reviews(sumOfReviews.orElse(null))
                .tier(member.getTier())
                .authStep(member.getAuthStep())
                .profileImage(member.getProfileImage())
                .rank(rank)
                .tokenAmount(amount)
                .blockchainUrl(member.getBlockchainUrl())
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
                .reviews(dto.getReviews())
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
        return gitOrganizations.stream().map(GitOrganization::getId).distinct().collect(Collectors.toList());
    }

    private List<String> getGitRepoNames(List<GitRepo> gitRepos) {
        return gitRepos.stream().map(GitRepo::getId).distinct().collect(Collectors.toList());
    }
}
