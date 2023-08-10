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
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.service.ContributionService;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author 김승진
 * @description WebClient로의 요청을 처리하는 Service
 */

@TransactionService
@RequiredArgsConstructor
public class MemberClientService {
    private final GithubClient<MemberClientRequest, MemberCommitResponse> memberCommitClient;
    private final GithubClient<MemberClientRequest, MemberIssueResponse> memberIssueClient;
    private final GithubClient<MemberClientRequest, MemberPullRequestResponse> memberPullRequestClient;
    private final GithubClient<MemberClientRequest, MemberCodeReviewResponse> memberCodeReviewClient;
    private final GithubClient<MemberClientRequest, MemberRepoResponse[]> memberRepoClient;
    private final GithubClient<MemberClientRequest, MemberOrganizationResponse[]> memberOrganizationClient;
    private final GithubClient<MemberClientRequest, OrganizationRepoResponse[]> memberOrganizationRepoClient;
    private final GitOrganizationService gitOrganizationService;
    private final GitRepoMapper gitRepoMapper;
    private final ContributionService<Commit, Long> commitService;
    private final ContributionService<Issue, Long> issueService;
    private final ContributionService<PullRequest, Long> pullRequestService;
    private final ContributionService<CodeReview, Long> codeReviewService;
    private final GitRepoRepository gitRepoRepository; // todo 순환참조 해결 후 repository 대신 service 주입받기
    private final GitRepoMemberRepository gitRepoMemberRepository; // todo 순환참조 해결 후 repository 대신 service 주입받기
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void addMemberContribution(final Member member) {
        int year = LocalDate.now().getYear();
        String githubId = member.getGithubId();

        MemberClientRequest request = new MemberClientRequest(githubId,  member.getGithubToken(), year);

        requestCommitClientAndSave(member, request);
        requestIssueClientAndSave(member, request);
        requestPullRequestClientAndSave(member, request);
        requestCodeReviewClientAndSave(member, request);
    }

    private void requestCodeReviewClientAndSave(final Member member, final MemberClientRequest request) {
        int codeReviewNum = memberCodeReviewClient.requestToGithub(request).getTotalCount();
        codeReviewService.saveContribution(member, codeReviewNum, request.getYear());
    }

    private void requestPullRequestClientAndSave(final Member member, final MemberClientRequest request) {
        int pullRequestNum = memberPullRequestClient.requestToGithub(request).getTotalCount();
        pullRequestService.saveContribution(member, pullRequestNum, request.getYear());
    }

    private void requestIssueClientAndSave(final Member member, final MemberClientRequest request) {
        int issueNum = memberIssueClient.requestToGithub(request).getTotalCount();
        issueService.saveContribution(member, issueNum, request.getYear());
    }

    private void requestCommitClientAndSave(final Member member, final MemberClientRequest request) {
        int commitNum = memberCommitClient.requestToGithub(request).getTotalCount();
        commitService.saveContribution(member, commitNum, request.getYear());
    }

    public void addMemberGitRepoAndGitOrganization(final Member member) {
        MemberClientRequest request = new MemberClientRequest(
                member.getGithubId(),
                member.getGithubToken(),
                LocalDate.now().getYear());

        saveGitRepos(findMemberRepoNames(request), member);
        gitOrganizationService.findAndSaveGitOrganizations(getMemberOrganizationNames(request), member);
    }

    public Set<String> findMemberRepoNames(final MemberClientRequest request) {
        return Arrays.stream(memberRepoClient.requestToGithub(request))
                .map(MemberRepoResponse::getFullName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<MemberOrganizationResponse> getMemberOrganizationNames(final MemberClientRequest request) {
        return Arrays.stream(memberOrganizationClient.requestToGithub(request))
                .filter(response -> response != null && response.getLogin() != null && response.getAvatarUrl() != null)
                .collect(Collectors.toSet());
    }

    private void saveGitRepos(final Set<String> gitRepoNames, final Member member) {
        Set<GitRepo> gitRepos = findIfGitRepoNotExists(gitRepoNames);
        saveAllGitRepos(gitRepos);

        Set<GitRepoMember> gitRepoMembers = findIfGitRepoMemberNotExists(member, gitRepos);
        saveAllGitRepoMembers(gitRepoMembers);
    }

    private void saveAllGitRepoMembers(final Set<GitRepoMember> gitRepoMembers) {
        gitRepoMemberRepository.saveAll(gitRepoMembers);
    }

    private Set<GitRepoMember> findIfGitRepoMemberNotExists(final Member member, final Set<GitRepo> gitRepos) {
        return gitRepos.stream()
                .filter(gitRepo -> !gitRepoMemberRepository.existsByGitRepoAndMember(gitRepo, member))
                .map(gr -> gitRepoMemberMapper.toEntity(member, gr))
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
        OrganizationRepoResponse[] clientResponse = memberOrganizationRepoClient.requestToGithub(new MemberClientRequest(gitOrganizationName, githubToken, LocalDate.now().getYear()));

        return Arrays.stream(clientResponse)
                .map(OrganizationRepoResponse::getFullName)
                .collect(Collectors.toList());
    }
}
