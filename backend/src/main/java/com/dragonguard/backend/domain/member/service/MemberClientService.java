package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.member.dto.client.*;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.template.client.GithubClient;
import com.dragonguard.backend.global.template.service.TransactionService;
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
    private final GithubClient<MemberClientRequest, OrganizationRepoResponse[]> organizationRepositoryClient;
    private final GithubClient<MemberClientRequest, MemberRepoResponse[]> repositoryClient;
    private final GithubClient<MemberClientRequest, MemberOrganizationResponse[]> organizationClient;
    private final GithubClient<MemberClientRequest, MemberCodeReviewResponse> codeReviewClient;
    private final GithubClient<MemberClientRequest, MemberPullRequestResponse> pullRequestClient;
    private final GithubClient<MemberClientRequest, MemberIssueResponse> issueClient;
    private final GithubClient<MemberClientRequest, MemberCommitResponse> commitClient;

    private final MemberContributionService memberContributionService;
    private final GitOrganizationGitRepoService gitOrganizationGitRepoService;

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
        final int codeReviewNum = codeReviewClient.requestToGithub(request).getTotalCount();

        memberContributionService.saveCodeReview(member, codeReviewNum, request.getYear());
    }

    private void requestPullRequestClientAndSave(final Member member, final MemberClientRequest request) {
        final int pullRequestNum = pullRequestClient.requestToGithub(request).getTotalCount();

        memberContributionService.savePullRequest(member, pullRequestNum, request.getYear());
    }

    private void requestIssueClientAndSave(final Member member, final MemberClientRequest request) {
        final int issueNum = issueClient.requestToGithub(request).getTotalCount();

        memberContributionService.saveIssue(member, issueNum, request.getYear());
    }

    private void requestCommitClientAndSave(final Member member, final MemberClientRequest request) {
        final int commitNum = commitClient.requestToGithub(request).getTotalCount();

        memberContributionService.saveCommit(member, commitNum, request.getYear());
    }

    public void addMemberGitRepoAndGitOrganization(final Member member) {
        final String githubToken = member.getGithubToken();
        final MemberClientRequest request = new MemberClientRequest(
                member.getGithubId(),
                githubToken,
                LocalDate.now().getYear());

        final Set<MemberOrganizationResponse> organizationResponses = getMemberOrganizationNames(request);
        gitOrganizationGitRepoService.saveAll(findMemberRepoNames(request), organizationResponses, member);
    }

    private Set<String> findMemberRepoNames(final MemberClientRequest request) {
        return Arrays.stream(repositoryClient.requestToGithub(request))
                .map(MemberRepoResponse::getFullName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<MemberOrganizationResponse> getMemberOrganizationNames(final MemberClientRequest request) {
        return Arrays.stream(organizationClient.requestToGithub(request))
                .filter(this::isValidResponse)
                .collect(Collectors.toSet());
    }

    private boolean isValidResponse(final MemberOrganizationResponse response) {
        return response != null && response.getLogin() != null && response.getAvatarUrl() != null;
    }

    public Set<String> requestGitOrganizationResponse(final String githubToken, final String gitOrganizationName) {
        final OrganizationRepoResponse[] clientResponse
                = organizationRepositoryClient.requestToGithub(new MemberClientRequest(gitOrganizationName, githubToken, LocalDate.now().getYear()));

        return Arrays.stream(clientResponse)
                .map(OrganizationRepoResponse::getFullName)
                .collect(Collectors.toSet());
    }

    public Object test() {
        return null;
    }
}
