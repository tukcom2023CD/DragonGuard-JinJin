package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.Author;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.Week;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoContributions;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.*;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.template.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@TransactionService
@RequiredArgsConstructor
public class GitRepoMemberFacade {
    private final GitRepoService gitRepoService;
    private final GitRepoMemberService gitRepoMemberService;
    private final AuthService authService;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request) {
        return gitRepoService.findTwoGitRepos(request);
    }

    public TwoGitRepoResponse findTwoGitReposAndUpdate(final GitRepoCompareRequest request) {
        return gitRepoService.findTwoGitReposAndUpdate(request);
    }

    public GitRepo findEntityByName(final String name) {
        return gitRepoService.findGitRepo(name);
    }

    public GitRepoMemberCompareResponse findTwoGitRepoMember(final GitRepoMemberCompareRequest request) {
        final GitRepoMember firstGitRepoMember =
                gitRepoMemberService.findByGitRepoAndMemberGithubId(gitRepoService.findGitRepo(request.getFirstRepo()), request.getFirstGithubId());

        final GitRepoMember secondGitRepoMember =
                gitRepoMemberService.findByGitRepoAndMemberGithubId(gitRepoService.findGitRepo(request.getSecondRepo()), request.getSecondGithubId());

        return new GitRepoMemberCompareResponse(
                gitRepoMemberMapper.toResponse(firstGitRepoMember),
                gitRepoMemberMapper.toResponse(secondGitRepoMember));
    }

    public List<GitRepoMemberResponse> requestToGithub(final GitRepoInfoRequest gitRepoInfoRequest, final GitRepo gitRepo) {
        return gitRepoService.requestClientGitRepoMember(gitRepoInfoRequest)
                .map(HashSet::new)
                .filter(response -> !this.hasEmptyProfileUrl(response))
                .map(contribution -> saveAndGetResult(gitRepo, contribution))
                .orElseGet(List::of);
    }

    private List<GitRepoMemberResponse> saveAndGetResult(final GitRepo gitRepo, final Set<GitRepoMemberClientResponse> contributions) {
        final List<GitRepoMemberResponse> result = getResponseList(
                contributions,
                gitRepoService.getContributionMap(contributions, Week::getA),
                gitRepoService.getContributionMap(contributions, Week::getD));

        gitRepoMemberService.saveAllIfNotExists(result, gitRepo);
        return result;
    }

    private boolean hasEmptyProfileUrl(final Set<GitRepoMemberClientResponse> contributions) {
        return contributions.isEmpty() || contributions.stream().anyMatch(c -> !StringUtils.hasText(c.getAuthor().getAvatarUrl()));
    }

    private List<GitRepoMemberResponse> getResponseList(
            final Set<GitRepoMemberClientResponse> contributions,
            final GitRepoContributions additions,
            final GitRepoContributions deletions) {

        return contributions.stream()
                .filter(this::hasNoEmptyValue)
                .map(clientResponse -> {
                    Author author = clientResponse.getAuthor();
                    String githubId = author.getLogin();
                    return new GitRepoMemberResponse(
                            githubId,
                            author.getAvatarUrl(),
                            clientResponse.getTotal(),
                            additions.getContributionByKey(clientResponse),
                            deletions.getContributionByKey(clientResponse),
                            gitRepoMemberService.isServiceMember(githubId));
                }).collect(Collectors.toList());
    }

    private boolean hasNoEmptyValue(final GitRepoMemberClientResponse c) {
        return c.getWeeks() != null && !c.getWeeks().isEmpty() && c.getTotal() != null && c.getAuthor() != null;
    }

    private List<GitRepoMemberResponse> findMembersByGitRepoWithClient(final GitRepoInfoRequest gitRepoInfoRequest) {
        final GitRepo gitRepo = gitRepoService.findGitRepo(gitRepoInfoRequest.getName());
        return requestToGithub(gitRepoInfoRequest, gitRepo);
    }

    private List<GitRepoMemberResponse> findGitRepoMemberResponses(final String repo, final Integer year, final String githubToken) {
        return findMembersByGitRepoWithClient(new GitRepoInfoRequest(githubToken, repo, year));
    }

    private TwoGitRepoMemberResponse getTwoGitRepoMemberResponse(final GitRepoCompareRequest gitRepoCompareRequest, final Integer year, final String githubToken) {
        return new TwoGitRepoMemberResponse(findGitRepoMemberResponses(gitRepoCompareRequest.getFirstRepo(), year, githubToken),
                findGitRepoMemberResponses(gitRepoCompareRequest.getSecondRepo(), year, githubToken));
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompareAndUpdate(final GitRepoCompareRequest gitRepoCompareRequest) {
        return getTwoGitRepoMemberResponse(gitRepoCompareRequest,
                LocalDate.now().getYear(), authService.getLoginUser().getGithubToken());
    }

    private List<GitRepoMemberResponse> findGitRepoMemberResponses(final GitRepo gitRepo, final String githubToken) {
        final Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
        if (isGitRepoMemberValid(gitRepoMembers)) {
            gitRepoService.requestKafkaGitRepoInfo(githubToken, gitRepo.getName());
            return gitRepoMemberMapper.toResponseList(gitRepoMembers);
        }
        return requestToGithub(new GitRepoInfoRequest(githubToken, gitRepo.getName(), LocalDate.now().getYear()), gitRepo);
    }

    private boolean isGitRepoMemberValid(final Set<GitRepoMember> gitRepoMembers) {
        return !gitRepoMembers.isEmpty() && gitRepoMembers.stream().noneMatch(grm ->
                Objects.isNull(grm.getGitRepoContribution()) || Objects.isNull(grm.getMember().getProfileImage()));
    }

    public GitRepoResponse findGitRepoInfos(final String name) {
        final String githubToken = authService.getLoginUser().getGithubToken();
        final GitRepo gitRepo = gitRepoService.findGitRepo(name);

        return new GitRepoResponse(
                gitRepoService.updateAndGetSparkLine(name, githubToken, gitRepo),
                findGitRepoMemberResponses(gitRepo, githubToken));
    }
    public GitRepoResponse findGitRepoInfosAndUpdate(final String name) {
        if (gitRepoService.gitRepoExistsByName(name)) {
            final String githubToken = authService.getLoginUser().getGithubToken();
            final GitRepo gitRepo = gitRepoService.findGitRepo(name);
            return new GitRepoResponse(
                    gitRepoService.updateAndGetSparkLine(name, githubToken, gitRepo),
                    findGitRepoMemberResponses(gitRepo, githubToken));
        }

        final String githubToken = authService.getLoginUser().getGithubToken();

        return new GitRepoResponse(
                gitRepoService.updateAndGetSparkLine(name, githubToken, gitRepoService.findGitRepo(name)),
                findGitRepoMemberResponses(name, LocalDate.now().getYear(), githubToken));
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(final GitRepoCompareRequest request) {
        final String githubToken = authService.getLoginUser().getGithubToken();
        return new TwoGitRepoMemberResponse(
                findGitRepoMemberResponses(gitRepoService.findGitRepo(request.getFirstRepo()), githubToken),
                findGitRepoMemberResponses(gitRepoService.findGitRepo(request.getSecondRepo()), githubToken));
    }

    public void saveAllGitRepoMembers(final Member member, final Set<String> gitRepoNames) {
        final Set<GitRepo> gitRepos = gitRepoNames.stream()
                .map(gitRepoService::findGitRepo)
                .collect(Collectors.toSet());

        gitRepoMemberService.saveAllIfNotExists(member, gitRepos);
    }

    public void saveAllGitRepos(final Set<String> gitRepoNames) {
        gitRepoService.saveAllIfNotExists(gitRepoNames);
    }

    public void updateOrSaveAll(final List<GitRepoMemberResponse> gitRepoMemberResponses, final String gitRepoName) {
        final GitRepo gitRepo = gitRepoService.findGitRepo(gitRepoName);
        gitRepoMemberService.updateOrSaveAll(gitRepoMemberResponses, gitRepo);
    }
}
