package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.Author;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.Week;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoContributionMap;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.*;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.service.TransactionService;
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

    public GitRepo getEntityByName(final String name) {
        return gitRepoService.findGitRepo(name);
    }

    public GitRepoMemberCompareResponse findTwoGitRepoMember(final GitRepoMemberCompareRequest request) {
        GitRepoMember firstGitRepoMember =
                gitRepoMemberService.findByGitRepoAndMemberGithubId(gitRepoService.findGitRepo(request.getFirstRepo()), request.getFirstGithubId());

        GitRepoMember secondGitRepoMember =
                gitRepoMemberService.findByGitRepoAndMemberGithubId(gitRepoService.findGitRepo(request.getSecondRepo()), request.getSecondGithubId());

        return new GitRepoMemberCompareResponse(
                gitRepoMemberMapper.toResponse(firstGitRepoMember),
                gitRepoMemberMapper.toResponse(secondGitRepoMember));
    }

    public List<GitRepoMemberResponse> requestToGithub(final GitRepoInfoRequest gitRepoInfoRequest, final GitRepo gitRepo) {
        Optional<List<GitRepoMemberClientResponse>> responses = gitRepoService.requestClientGitRepoMember(gitRepoInfoRequest);
        if (responses.isEmpty()) {
            return List.of();
        }
        Set<GitRepoMemberClientResponse> contributions = new HashSet<>(responses.get());
        if (contributions.isEmpty() || contributions.stream().anyMatch(c -> !StringUtils.hasText(c.getAuthor().getAvatarUrl()))) {
            return List.of();
        }

        List<GitRepoMemberResponse> result = getResponseList(
                contributions,
                gitRepoService.getContributionMap(contributions, Week::getA),
                gitRepoService.getContributionMap(contributions, Week::getD));

        gitRepoMemberService.saveAll(result, gitRepo);
        return result;
    }

    private List<GitRepoMemberResponse> getResponseList(
            final Set<GitRepoMemberClientResponse> contributions,
            final GitRepoContributionMap additions,
            final GitRepoContributionMap deletions) {

        return contributions.stream()
                .filter(c -> c.getWeeks() != null && !c.getWeeks().isEmpty() && c.getTotal() != null && c.getAuthor() != null)
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

    private List<GitRepoMemberResponse> findMembersByGitRepoWithClient(final GitRepoInfoRequest gitRepoInfoRequest) {
        GitRepo gitRepo = gitRepoService.findGitRepo(gitRepoInfoRequest.getName());
        return requestToGithub(gitRepoInfoRequest, gitRepo);
    }

    private List<GitRepoMemberResponse> getGitRepoMemberResponses(final String repo, final Integer year, final String githubToken) {
        return findMembersByGitRepoWithClient(new GitRepoInfoRequest(githubToken, repo, year));
    }

    private TwoGitRepoMemberResponse getTwoGitRepoMemberResponse(final GitRepoCompareRequest gitRepoCompareRequest, final Integer year, final String githubToken) {
        return new TwoGitRepoMemberResponse(getGitRepoMemberResponses(gitRepoCompareRequest.getFirstRepo(), year, githubToken),
                getGitRepoMemberResponses(gitRepoCompareRequest.getSecondRepo(), year, githubToken));
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompareAndUpdate(final GitRepoCompareRequest gitRepoCompareRequest) {
        return getTwoGitRepoMemberResponse(gitRepoCompareRequest,
                LocalDate.now().getYear(), authService.getLoginUser().getGithubToken());
    }

    private List<GitRepoMemberResponse> getGitRepoMemberResponses(final GitRepo gitRepo, final String githubToken) {
        Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
        if (isGitRepoMemberValid(gitRepoMembers)) {
            gitRepoService.requestKafkaGitRepoInfo(githubToken, gitRepo.getName());
            return gitRepoMemberMapper.toResponseList(gitRepoMembers);
        }
        return requestToGithub(new GitRepoInfoRequest(githubToken, gitRepo.getName(), LocalDate.now().getYear()), gitRepo);
    }

    private boolean isGitRepoMemberValid(final Set<GitRepoMember> gitRepoMembers) {
        return !gitRepoMembers.isEmpty() && gitRepoMembers.stream().noneMatch(grm ->
                Objects.isNull(grm.getGitRepoContribution()) || StringUtils.hasText(grm.getMember().getProfileImage()));
    }

    public GitRepoResponse findGitRepoInfos(final String name) {
        String githubToken = authService.getLoginUser().getGithubToken();
        GitRepo gitRepo = gitRepoService.findGitRepo(name);

        return new GitRepoResponse(
                gitRepoService.updateAndGetSparkLine(name, githubToken, gitRepo),
                getGitRepoMemberResponses(gitRepo, githubToken));
    }
    public GitRepoResponse findGitRepoInfosAndUpdate(final String name) {
        if (gitRepoService.gitRepoExistsByName(name)) {
            String githubToken = authService.getLoginUser().getGithubToken();
            GitRepo gitRepo = gitRepoService.findGitRepo(name);
            return new GitRepoResponse(
                    gitRepoService.updateAndGetSparkLine(name, githubToken, gitRepo),
                    getGitRepoMemberResponses(gitRepo, githubToken));
        }

        int year = LocalDate.now().getYear();
        String githubToken = authService.getLoginUser().getGithubToken();

        return new GitRepoResponse(
                gitRepoService.updateAndGetSparkLine(name, githubToken, gitRepoService.findGitRepo(name)),
                getGitRepoMemberResponses(name, year, githubToken));
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(final GitRepoCompareRequest request) {
        String githubToken = authService.getLoginUser().getGithubToken();
        return new TwoGitRepoMemberResponse(
                getGitRepoMemberResponses(gitRepoService.findGitRepo(request.getFirstRepo()), githubToken),
                getGitRepoMemberResponses(gitRepoService.findGitRepo(request.getSecondRepo()), githubToken));
    }
}
