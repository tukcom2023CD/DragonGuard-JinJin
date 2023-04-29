package com.dragonguard.backend.member.service;

import com.dragonguard.backend.contribution.dto.response.CommitScrapingResponse;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.issue.service.IssueService;
import com.dragonguard.backend.member.dto.request.client.MemberClientRequest;
import com.dragonguard.backend.member.dto.response.client.*;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.pullrequest.service.PullRequestService;
import com.dragonguard.backend.util.GithubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 김승진
 * @description WebClient로의 요청을 처리하는 Service
 */

@Service
@Transactional
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

    public void addMemberContribution(Member member) {
        int year = LocalDate.now().getYear();
        String githubId = member.getGithubId();
        String githubToken = member.getGithubToken();
        MemberClientRequest request = new MemberClientRequest(githubId, githubToken, year);

        int commitNum = memberCommitClient.requestToGithub(request).getTotal_count();
        int issueNum = memberIssueClient.requestToGithub(request).getTotal_count();
        int pullRequestNum = memberPullRequestClient.requestToGithub(request).getTotal_count();

        commitService.saveCommits(new CommitScrapingResponse(githubId, commitNum));
        issueService.saveIssues(githubId, issueNum, year);
        pullRequestService.savePullRequests(githubId, pullRequestNum, year);
    }

    public void addMemberGitRepoAndGitOrganization(Member member) {
        int year = LocalDate.now().getYear();
        String githubId = member.getGithubId();
        String githubToken = member.getGithubToken();
        MemberClientRequest request = new MemberClientRequest(githubId, githubToken, year);

        List<String> memberRepoNames = Arrays.stream(memberRepoClient.requestToGithub(request))
                .map(MemberRepoResponse::getFull_name)
                .collect(Collectors.toList());
        List<String> memberOrganizationNames = Arrays.stream(memberOrganizationClient.requestToGithub(request))
                .map(MemberOrganizationResponse::getLogin)
                .collect(Collectors.toList());

        saveGitRepos(memberRepoNames);
        gitOrganizationService.saveGitOrganizations(memberOrganizationNames, member);
    }

    public void addGitOrganizationRepo(String orgName, Member member) {
        List<String> repoNames = Arrays.stream(organizationRepoClient.requestToGithub(new MemberClientRequest(orgName, member.getGithubToken(), LocalDate.now().getYear())))
                .map(OrganizationRepoResponse::getFull_name)
                .collect(Collectors.toList());

        saveGitRepos(repoNames);
    }

    public void saveGitRepos(List<String> gitRepoNames) {
        List<GitRepo> gitRepos = gitRepoNames.stream()
                .filter(name -> !gitRepoRepository.existsByName(name))
                .map(gitRepoMapper::toEntity)
                .collect(Collectors.toList());

        gitRepoRepository.saveAll(gitRepos);
    }
}
