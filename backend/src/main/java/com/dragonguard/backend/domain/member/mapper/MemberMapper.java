package com.dragonguard.backend.domain.member.mapper;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.dto.response.MemberGitOrganizationResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberGitReposAndGitOrganizationsResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.organization.dto.response.RelatedRankWithMemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(target = "githubId", source = "memberRequest.githubId")
    Member toEntity(final MemberRequest memberRequest, final AuthStep authStep);

    Member toEntity(final String githubId, final Role role, final AuthStep authStep);

    @Mapping(target = "organizationRank", source = "relatedRank.organizationRank")
    @Mapping(target = "memberGithubIds", source = "relatedRank.memberGithubIds")
    @Mapping(target = "isLast", source = "relatedRank.isLast")
    @Mapping(target = "organization", source = "organization")
    @Mapping(target = "commits", expression = "java(member.getSumOfCommits().orElse(null))")
    @Mapping(target = "issues", expression = "java(member.getSumOfIssues().orElse(null))")
    @Mapping(target = "pullRequests", expression = "java(member.getSumOfPullRequests().orElse(null))")
    @Mapping(target = "reviews", expression = "java(member.getSumOfReviews().orElse(null))")
    MemberResponse toResponse(final Member member, final Integer rank, final Long tokenAmount, final String organization, final RelatedRankWithMemberResponse relatedRank);

    @Mapping(target = "tokenAmount", source = "amount")
    @Mapping(target = "organization", source = "member.organization.name")
    @Mapping(target = "commits", expression = "java(member.getSumOfCommits().orElse(null))")
    @Mapping(target = "issues", expression = "java(member.getSumOfIssues().orElse(null))")
    @Mapping(target = "pullRequests", expression = "java(member.getSumOfPullRequests().orElse(null))")
    @Mapping(target = "reviews", expression = "java(member.getSumOfReviews().orElse(null))")
    MemberResponse toResponse(final Member member, final Integer rank, final Long amount);

    @Mapping(target = "gitOrganizations", source = "gitOrganizations", qualifiedByName = "getGitOrganizationNames")
    @Mapping(target = "gitRepos", source = "gitRepos", qualifiedByName = "getGitRepoNames")
    @Mapping(target = "memberProfileImage", source = "memberProfileImage")
    MemberGitReposAndGitOrganizationsResponse toDetailResponse(final String memberProfileImage, final List<GitOrganization> gitOrganizations, final List<GitRepo> gitRepos);

    @Named("getGitOrganizationNames")
    default List<MemberGitOrganizationResponse> getGitOrganizationNames(final List<GitOrganization> gitOrganizations) {
        return gitOrganizations.stream().map(org -> new MemberGitOrganizationResponse(org.getName(), org.getProfileImage())).distinct().collect(Collectors.toList());
    }

    @Named("getGitRepoNames")
    default List<String> getGitRepoNames(final List<GitRepo> gitRepos) {
        return gitRepos.stream().map(GitRepo::getName).distinct().collect(Collectors.toList());
    }
}
