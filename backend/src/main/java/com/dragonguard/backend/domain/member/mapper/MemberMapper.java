package com.dragonguard.backend.domain.member.mapper;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.dto.response.MemberDetailsResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberGitOrganizationResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberGitReposAndGitOrganizationsResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.organization.dto.response.RelatedRankWithMemberResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버 Entity와 dto의 변환을 돕는 클래스
 */
@Mapper(
        componentModel = ComponentModel.SPRING,
        imports = {GitRepo.class, GitRepoMember.class, Collectors.class, Optional.class})
public interface MemberMapper {
    @Mapping(target = "githubId", source = "githubId")
    @Mapping(target = "profileImage", source = "profileUrl")
    @Mapping(target = "authStep", source = "authStep")
    Member toEntity(final String githubId, final AuthStep authStep, final String profileUrl);

    Member toEntity(final String githubId, final Role role, final AuthStep authStep);

    Member toEntity(
            final String githubId,
            final Role role,
            final AuthStep authStep,
            final String name,
            final String profileImage);

    @Mapping(target = "organizationRank", source = "relatedRank.organizationRank")
    @Mapping(target = "memberGithubIds", source = "relatedRank.memberGithubIds")
    @Mapping(
            target = "tokenAmount",
            expression = "java(Optional.ofNullable(member.getSumOfTokens()).orElse(0L))")
    @Mapping(target = "isLast", source = "relatedRank.isLast")
    @Mapping(target = "organization", source = "organization")
    @Mapping(
            target = "commits",
            expression = "java(Optional.ofNullable(member.getSumOfCommits()).orElse(0))")
    @Mapping(
            target = "issues",
            expression = "java(Optional.ofNullable(member.getSumOfIssues()).orElse(0))")
    @Mapping(
            target = "pullRequests",
            expression = "java(Optional.ofNullable(member.getSumOfPullRequests()).orElse(0))")
    @Mapping(
            target = "reviews",
            expression = "java(Optional.ofNullable(member.getSumOfCodeReviews()).orElse(0))")
    MemberResponse toResponse(
            final Member member,
            final Integer rank,
            final String organization,
            final RelatedRankWithMemberResponse relatedRank);

    @Mapping(target = "rank", source = "rank")
    @Mapping(
            target = "tokenAmount",
            expression = "java(Optional.ofNullable(member.getSumOfTokens()).orElse(0L))")
    @Mapping(target = "organization", source = "member.organization.name")
    @Mapping(
            target = "commits",
            expression = "java(Optional.ofNullable(member.getSumOfCommits()).orElse(0))")
    @Mapping(
            target = "issues",
            expression = "java(Optional.ofNullable(member.getSumOfIssues()).orElse(0))")
    @Mapping(
            target = "pullRequests",
            expression = "java(Optional.ofNullable(member.getSumOfPullRequests()).orElse(0))")
    @Mapping(
            target = "reviews",
            expression = "java(Optional.ofNullable(member.getSumOfCodeReviews()).orElse(0))")
    MemberResponse toResponse(final Member member, final Integer rank);

    @Mapping(
            target = "gitOrganizations",
            source = "gitOrganizations",
            qualifiedByName = "getGitOrganizationNames")
    @Mapping(target = "gitRepos", source = "gitRepos", qualifiedByName = "getGitRepoNames")
    @Mapping(target = "memberProfileImage", source = "memberProfileImage")
    MemberGitReposAndGitOrganizationsResponse toRepoAndOrgResponse(
            final String memberProfileImage,
            final List<GitOrganization> gitOrganizations,
            final List<GitRepo> gitRepos);

    @Mapping(
            target = "commits",
            expression = "java(Optional.ofNullable(member.getSumOfCommits()).orElse(0))")
    @Mapping(
            target = "issues",
            expression = "java(Optional.ofNullable(member.getSumOfIssues()).orElse(0))")
    @Mapping(
            target = "pullRequests",
            expression = "java(Optional.ofNullable(member.getSumOfPullRequests()).orElse(0))")
    @Mapping(
            target = "reviews",
            expression = "java(Optional.ofNullable(member.getSumOfCodeReviews()).orElse(0))")
    @Mapping(target = "profileImage", source = "member.profileImage")
    @Mapping(target = "gitRepos", expression = "java(member.getGitRepoNames())")
    @Mapping(target = "organization", expression = "java(member.getOrganizationName())")
    @Mapping(target = "rank", source = "rank")
    MemberDetailsResponse toDetailsResponse(final Member member, final Integer rank);

    @Named("getGitOrganizationNames")
    default Set<MemberGitOrganizationResponse> getGitOrganizationNames(
            final List<GitOrganization> gitOrganizations) {
        return gitOrganizations.stream()
                .map(org -> new MemberGitOrganizationResponse(org.getName(), org.getProfileImage()))
                .collect(Collectors.toSet());
    }

    @Named("getGitRepoNames")
    default Set<String> getGitRepoNames(final List<GitRepo> gitRepos) {
        return gitRepos.stream().map(GitRepo::getName).collect(Collectors.toSet());
    }
}
