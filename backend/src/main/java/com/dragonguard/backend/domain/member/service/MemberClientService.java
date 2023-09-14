package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.member.dto.client.*;
import com.dragonguard.backend.domain.member.dto.constant.ContributionServiceComponent;
import com.dragonguard.backend.domain.member.dto.constant.GithubClientComponent;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.service.ContributionService;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 김승진
 * @description WebClient로의 요청을 처리하는 Service
 */

@TransactionService
@RequiredArgsConstructor
public class MemberClientService {
    private final Map<String, GithubClient> githubClients;
    private final Map<String, ContributionService> contributionServices;
    private final GitOrganizationService gitOrganizationService;
    private final GitRepoMapper gitRepoMapper;
    private final GitRepoRepository gitRepoRepository;              // todo 순환참조 해결
    private final GitRepoMemberRepository gitRepoMemberRepository;  // todo 순환참조 해결
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void addMemberContribution(final Member member) {
        final int year = LocalDate.now().getYear();
        final String githubId = member.getGithubId();
        final MemberClientRequest request = new MemberClientRequest(githubId,  member.getGithubToken(), year);

        requestCommitClientAndSave(member, request);
        requestIssueClientAndSave(member, request);
        requestPullRequestClientAndSave(member, request);
        requestCodeReviewClientAndSave(member, request);
    }

    private void requestCodeReviewClientAndSave(final Member member, final MemberClientRequest request) {
        final int codeReviewNum = getGithubClient(GithubClientComponent.CODE_REVIEW_COMPONENT.getName(), MemberCodeReviewResponse.class)
                .requestToGithub(request).getTotalCount();

        getContributionService(ContributionServiceComponent.CODE_REVIEW_COMPONENT.getName(), CodeReview.class)
                .saveContribution(member, codeReviewNum, request.getYear());
    }

    private void requestPullRequestClientAndSave(final Member member, final MemberClientRequest request) {
        final int pullRequestNum = getGithubClient(GithubClientComponent.PULL_REQUEST_COMPONENT.getName(), MemberPullRequestResponse.class)
                .requestToGithub(request).getTotalCount();

        getContributionService(ContributionServiceComponent.PULL_REQUEST_COMPONENT.getName(), PullRequest.class)
                .saveContribution(member, pullRequestNum, request.getYear());
    }

    private void requestIssueClientAndSave(final Member member, final MemberClientRequest request) {
        final int issueNum = getGithubClient(GithubClientComponent.ISSUE_COMPONENT.getName(), MemberIssueResponse.class)
                .requestToGithub(request).getTotalCount();

        getContributionService(ContributionServiceComponent.ISSUE_COMPONENT.getName(), Issue.class)
                .saveContribution(member, issueNum, request.getYear());
    }

    private void requestCommitClientAndSave(final Member member, final MemberClientRequest request) {
        final int commitNum = getGithubClient(GithubClientComponent.COMMIT_COMPONENT.getName(), MemberCommitResponse.class)
                .requestToGithub(request).getTotalCount();

        getContributionService(ContributionServiceComponent.COMMIT_COMPONENT.getName(), Commit.class)
                .saveContribution(member, commitNum, request.getYear());
    }

    public void addMemberGitRepoAndGitOrganization(final Member member) {
        final MemberClientRequest request = new MemberClientRequest(
                member.getGithubId(),
                member.getGithubToken(),
                LocalDate.now().getYear());

        saveGitRepoAndGitRepoMember(findMemberRepoNames(request), member);
        gitOrganizationService.findAndSaveGitOrganizations(getMemberOrganizationNames(request), member);
    }

    public Set<String> findMemberRepoNames(final MemberClientRequest request) {
        return Arrays.stream(getGithubClient("memberRepoClient", MemberRepoResponse[].class).requestToGithub(request))
                .map(MemberRepoResponse::getFullName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<MemberOrganizationResponse> getMemberOrganizationNames(final MemberClientRequest request) {
        return Arrays.stream(getGithubClient("memberOrganizationClient", MemberOrganizationResponse[].class).requestToGithub(request))
                .filter(this::isValidResponse)
                .collect(Collectors.toSet());
    }

    private boolean isValidResponse(final MemberOrganizationResponse response) {
        return response != null && response.getLogin() != null && response.getAvatarUrl() != null;
    }

    private void saveGitRepoAndGitRepoMember(final Set<String> gitRepoNames, final Member member) {
        final Set<GitRepo> gitRepos = findIfGitRepoNotExists(gitRepoNames);
        saveAllGitRepos(gitRepos);

        final Set<GitRepoMember> gitRepoMembers = findIfGitRepoMemberNotExists(member, gitRepos);
        saveAllGitRepoMembers(gitRepoMembers);
    }

    private void saveAllGitRepoMembers(final Set<GitRepoMember> gitRepoMembers) {
        gitRepoMemberRepository.saveAll(gitRepoMembers);
    }

    private Set<GitRepoMember> findIfGitRepoMemberNotExists(final Member member, final Set<GitRepo> gitRepos) {
        return gitRepos.stream()
                .filter(gitRepo -> !gitRepoMemberRepository.existsByGitRepoAndMember(gitRepo, member))
                .map(gitRepo -> gitRepoMemberMapper.toEntity(member, gitRepo))
                .collect(Collectors.toSet());
    }

    private void saveAllGitRepos(final Set<GitRepo> gitRepos) {
        gitRepoRepository.saveAll(gitRepos);
    }

    private Set<GitRepo> findIfGitRepoNotExists(final Set<String> gitRepoNames) {
        return gitRepoNames.stream()
                .filter(name -> !gitRepoRepository.existsByName(name))
                .map(gitRepoMapper::toEntity)
                .collect(Collectors.toSet());
    }

    public List<String> requestGitOrganizationResponse(final String githubToken, final String gitOrganizationName) {
        final OrganizationRepoResponse[] clientResponse
                = getGithubClient("memberOrganizationRepoClient", OrganizationRepoResponse[].class)
                .requestToGithub(new MemberClientRequest(gitOrganizationName, githubToken, LocalDate.now().getYear()));

        return Arrays.stream(clientResponse)
                .map(OrganizationRepoResponse::getFullName)
                .collect(Collectors.toList());
    }

    private <T> GithubClient<MemberClientRequest, T> getGithubClient(final String componentName, final Class<T> type) {
        return (GithubClient<MemberClientRequest, T>) githubClients.get(componentName);
    }

    private <T> ContributionService<T, Long> getContributionService(final String componentName, final Class<T> type) {
        return (ContributionService<T, Long>) contributionServices.get(componentName);
    }
}
