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
import com.dragonguard.backend.global.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring", imports = {GitRepo.class, GitRepoMember.class, Collectors.class})
public interface MemberMapper extends EntityMapper {
    @Mapping(target = "githubId", source = "githubId")
    @Mapping(target = "profileImage", source = "profileUrl")
    @Mapping(target = "authStep", source = "authStep")
    Member toEntity(final String githubId, final AuthStep authStep, final String profileUrl);

    Member toEntity(final String githubId, final Role role, final AuthStep authStep);

    Member toEntity(final String githubId, final Role role, final AuthStep authStep, final String name, final String profileImage);

    @Mapping(target = "organizationRank", source = "relatedRank.organizationRank")
    @Mapping(target = "memberGithubIds", source = "relatedRank.memberGithubIds")
    @Mapping(target = "tokenAmount", expression = "java(member.getSumOfTokens())")
    @Mapping(target = "isLast", source = "relatedRank.isLast")
    @Mapping(target = "organization", source = "organization")
    @Mapping(target = "commits", expression = "java(member.getSumOfCommits().orElse(null))")
    @Mapping(target = "issues", expression = "java(member.getSumOfIssues().orElse(null))")
    @Mapping(target = "pullRequests", expression = "java(member.getSumOfPullRequests().orElse(null))")
    @Mapping(target = "reviews", expression = "java(member.getSumOfCodeReviews().orElse(null))")
    MemberResponse toResponse(final Member member, final Integer rank, final String organization, final RelatedRankWithMemberResponse relatedRank);

    @Mapping(target = "rank", source = "rank")
    @Mapping(target = "tokenAmount", expression = "java(member.getSumOfTokens())")
    @Mapping(target = "organization", source = "member.organization.name")
    @Mapping(target = "commits", expression = "java(member.getSumOfCommits().orElse(null))")
    @Mapping(target = "issues", expression = "java(member.getSumOfIssues().orElse(null))")
    @Mapping(target = "pullRequests", expression = "java(member.getSumOfPullRequests().orElse(null))")
    @Mapping(target = "reviews", expression = "java(member.getSumOfCodeReviews().orElse(null))")
    MemberResponse toResponse(final Member member, final Integer rank);

    @Mapping(target = "gitOrganizations", source = "gitOrganizations", qualifiedByName = "getGitOrganizationNames")
    @Mapping(target = "gitRepos", source = "gitRepos", qualifiedByName = "getGitRepoNames")
    @Mapping(target = "memberProfileImage", source = "memberProfileImage")
    MemberGitReposAndGitOrganizationsResponse toRepoAndOrgResponse(final String memberProfileImage, final List<GitOrganization> gitOrganizations, final List<GitRepo> gitRepos);

    @Mapping(target = "commits", expression = "java(member.getSumOfCommits().orElse(null))")
    @Mapping(target = "issues", expression = "java(member.getSumOfIssues().orElse(null))")
    @Mapping(target = "pullRequests", expression = "java(member.getSumOfPullRequests().orElse(null))")
    @Mapping(target = "reviews", expression = "java(member.getSumOfCodeReviews().orElse(null))")
    @Mapping(target = "profileImage", source = "member.profileImage")
    @Mapping(target = "gitRepos", expression = "java(member.getGitRepoMembers().stream().map(GitRepoMember::getGitRepo).map(GitRepo::getName).collect(Collectors.toList()))")
    @Mapping(target = "organization", expression = "java(member.getOrganization() != null ? member.getOrganization().getName() : null)")
    @Mapping(target = "rank", source = "rank")
    MemberDetailsResponse toDetailsResponse(Member member, Integer rank);

    @Named("getGitOrganizationNames")
    default List<MemberGitOrganizationResponse> getGitOrganizationNames(final List<GitOrganization> gitOrganizations) {
        return gitOrganizations.stream().map(org -> new MemberGitOrganizationResponse(org.getName(), org.getProfileImage())).distinct().collect(Collectors.toList());
    }

    @Named("getGitRepoNames")
    default List<String> getGitRepoNames(final List<GitRepo> gitRepos) {
        return gitRepos.stream().map(GitRepo::getName).distinct().collect(Collectors.toList());
    }
}
