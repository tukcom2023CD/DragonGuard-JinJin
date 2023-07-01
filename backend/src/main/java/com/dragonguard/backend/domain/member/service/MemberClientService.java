package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.commit.service.CommitService;
import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.issue.service.IssueService;
import com.dragonguard.backend.domain.member.dto.client.*;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.service.PullRequestService;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
    private final GithubClient<MemberClientRequest, MemberRepoResponse[]> memberRepoClient;
    private final GithubClient<MemberClientRequest, MemberOrganizationResponse[]> memberOrganizationClient;
    private final GithubClient<MemberClientRequest, OrganizationRepoResponse[]> memberOrganizationRepoClient;
    private final GitOrganizationService gitOrganizationService;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMapper gitRepoMapper;
    private final CommitService commitService;
    private final IssueService issueService;
    private final PullRequestService pullRequestService;
    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void addMemberContribution(final Member member) {
        int year = LocalDate.now().getYear();
        String githubId = member.getGithubId();

        MemberClientRequest request = new MemberClientRequest(githubId,  member.getGithubToken(), year);

        requestCommitClientAndSave(member, request);
        requestIssueClientAndSave(member, request);
        requestPullRequestClientAndSave(member, request);
    }

    private void requestPullRequestClientAndSave(final Member member, final MemberClientRequest request) {
        int pullRequestNum = memberPullRequestClient.requestToGithub(request).getTotalCount();
        pullRequestService.savePullRequests(member, pullRequestNum, request.getYear());
    }

    private void requestIssueClientAndSave(final Member member, final MemberClientRequest request) {
        int issueNum = memberIssueClient.requestToGithub(request).getTotalCount();
        issueService.saveIssues(member, issueNum, request.getYear());
    }

    private void requestCommitClientAndSave(final Member member, final MemberClientRequest request) {
        int commitNum = memberCommitClient.requestToGithub(request).getTotalCount();
        commitService.saveCommits(new ContributionScrapingResponse(member.getGithubId(), commitNum), member);
    }

    public void addMemberGitRepoAndGitOrganization(final Member member) {
        MemberClientRequest request = new MemberClientRequest(
                member.getGithubId(),
                member.getGithubToken(),
                LocalDate.now().getYear());

        saveGitRepos(getMemberRepoNames(request), member);
        gitOrganizationService.findAndSaveGitOrganizations(getMemberOrganizationNames(request), member);
    }

    public Set<String> getMemberRepoNames(final MemberClientRequest request) {
        return Arrays.stream(memberRepoClient.requestToGithub(request))
                .map(MemberRepoResponse::getFullName)
                .collect(Collectors.toSet());
    }

    private Set<MemberOrganizationResponse> getMemberOrganizationNames(final MemberClientRequest request) {
        return Arrays.stream(memberOrganizationClient.requestToGithub(request))
                .collect(Collectors.toSet());
    }

    private void saveGitRepos(final Set<String> gitRepoNames, final Member member) {
        Set<GitRepo> gitRepos = findIfGitRepoNotExists(gitRepoNames);
        saveAllGitRepos(gitRepos);

        Set<GitRepoMember> list = findIfGitRepoMemberNotExists(member, gitRepos);
        saveAllGitRepoMembers(list);
    }

    private void saveAllGitRepoMembers(final Set<GitRepoMember> list) {
        gitRepoMemberRepository.saveAll(list);
    }

    private Set<GitRepoMember> findIfGitRepoMemberNotExists(final Member member, final Set<GitRepo> gitRepos) {
        return gitRepos.stream()
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

    public List<String> requestGitOrganizationResponse(String githubToken, String gitOrganizationName) {
        OrganizationRepoResponse[] clientResponse = memberOrganizationRepoClient.requestToGithub(new MemberClientRequest(gitOrganizationName, githubToken, LocalDate.now().getYear()));

        return Arrays.stream(clientResponse)
                .map(OrganizationRepoResponse::getFullName)
                .collect(Collectors.toList());
    }
}
