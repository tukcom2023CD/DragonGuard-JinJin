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
    private final GithubClient<MemberClientRequest, OrganizationRepoResponse[]> organizationRepoClient;
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
        String githubToken = member.getGithubToken();
        MemberClientRequest request = new MemberClientRequest(githubId, githubToken, year);

        int commitNum = memberCommitClient.requestToGithub(request).getTotal_count();
        int issueNum = memberIssueClient.requestToGithub(request).getTotal_count();
        int pullRequestNum = memberPullRequestClient.requestToGithub(request).getTotal_count();

        commitService.saveCommits(new ContributionScrapingResponse(githubId, commitNum), member);
        issueService.saveIssues(member, issueNum, year);
        pullRequestService.savePullRequests(member, pullRequestNum, year);
    }

    public void addMemberGitRepoAndGitOrganization(final Member member) {
        int year = LocalDate.now().getYear();
        String githubId = member.getGithubId();
        String githubToken = member.getGithubToken();
        MemberClientRequest request = new MemberClientRequest(githubId, githubToken, year);

        Set<String> memberRepoNames = Arrays.stream(memberRepoClient.requestToGithub(request))
                .map(MemberRepoResponse::getFull_name)
                .collect(Collectors.toSet());
        Set<String> memberOrganizationNames = Arrays.stream(memberOrganizationClient.requestToGithub(request))
                .map(MemberOrganizationResponse::getLogin)
                .collect(Collectors.toSet());

        saveGitRepos(memberRepoNames, member);
        gitOrganizationService.saveGitOrganizations(memberOrganizationNames, member);
    }

    public void saveGitRepos(final Set<String> gitRepoNames, final Member member) {
        Set<GitRepo> gitRepos = gitRepoNames.stream()
                .map(name -> gitRepoRepository.findByName(name).orElseGet(() -> gitRepoMapper.toEntity(name)))
                .collect(Collectors.toSet());

        gitRepoRepository.saveAll(gitRepos);
        Set<GitRepoMember> list = gitRepos.stream()
                .map(gr -> gitRepoMemberMapper.toEntity(member, gr))
                .collect(Collectors.toSet());

        gitRepoMemberRepository.saveAll(list);
    }
}
