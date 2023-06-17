package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoSparkLineResponse;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.ClosedIssueKafkaResponse;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.StatisticsResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepoContributionMap;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepoLanguageMap;
import com.dragonguard.backend.domain.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.Week;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 서비스 로직을 처리하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class GitRepoService implements EntityLoader<GitRepo, Long> {
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberService gitRepoMemberService;
    private final MemberService memberService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaProducer<GitRepoNameRequest> kafkaIssueProducer;
    private final GithubClient<GitRepoInfoRequest, GitRepoMemberClientResponse[]> gitRepoMemberClient;
    private final GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    private final GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;
    private final GithubClient<GitRepoClientRequest, GitRepoSparkLineResponse> gitRepoSparkLineClient;

    public GitRepoResponse  findGitRepoInfosAndUpdate(final String name) {
        if (!gitRepoRepository.existsByName(name)) {
            return findGitRepoInfosAndUpdate(name);
        }
        int year = LocalDate.now().getYear();
        GitRepoInfoRequest gitRepoInfoRequest = new GitRepoInfoRequest(name, year);
        String githubToken = setGithubTokenAndGet(gitRepoInfoRequest);

        List<GitRepoMemberResponse> gitRepoMemberResponses = getGitRepoMemberResponses(name, year, githubToken);
        List<Integer> sparkLine = updateAndGetSparkLine(githubToken, name, getOrSaveGitRepo(name));

        return new GitRepoResponse(sparkLine, gitRepoMemberResponses);
    }

    private String setGithubTokenAndGet(final GitRepoInfoRequest gitRepoInfoRequest) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        gitRepoInfoRequest.setGithubToken(githubToken);
        return githubToken;
    }

    public List<Integer> updateAndGetSparkLine(final String name, final String githubToken, final GitRepo gitRepo) {
        List<Integer> sparkLine = Arrays.asList(requestClientSparkLine(githubToken, name).getAll());
        gitRepo.updateSparkLine(sparkLine);
        return sparkLine;
    }

    public void updateSparkLine(final String name, final String githubToken) {
        getOrSaveGitRepo(name).updateSparkLine(Arrays.asList(requestClientSparkLine(githubToken, name).getAll()));
    }

    private GitRepoSparkLineResponse requestClientSparkLine(final String githubToken, final String name) {
        return gitRepoSparkLineClient.requestToGithub(new GitRepoClientRequest(githubToken, name));
    }

    public GitRepo getOrSaveGitRepo(final String name) {
        return gitRepoRepository.findByName(name).orElse(gitRepoRepository.save(gitRepoMapper.toEntity(name)));
    }

    public List<GitRepoMemberResponse> findMembersByGitRepoWithClient(final GitRepoInfoRequest gitRepoInfoRequest) {
        Optional<GitRepo> optionalGitRepo = gitRepoRepository.findByName(gitRepoInfoRequest.getName());
        if (checkGitRepoIfValidAndSave(gitRepoInfoRequest.getName(), optionalGitRepo)) return requestToGithub(gitRepoInfoRequest);

        GitRepo gitRepo = optionalGitRepo.orElseThrow(EntityNotFoundException::new);
        if (isContributionNotValid(gitRepo.getGitRepoMembers())) return requestToGithub(gitRepoInfoRequest);

        return getGitRepoMemberResponses(gitRepo);
    }

    public boolean checkGitRepoIfValidAndSave(final String name, final Optional<GitRepo> gitRepo) {
        if (gitRepo.isEmpty()) {
            saveGitRepo(name);
            return true;
        }
        return false;
    }

    private boolean isContributionNotValid(final Set<GitRepoMember> gitRepoMembers) {
        return gitRepoMembers.isEmpty() || gitRepoMembers.stream().findFirst().orElseThrow(EntityNotFoundException::new).getGitRepoContribution() == null;
    }

    private List<GitRepoMemberResponse> getGitRepoMemberResponses(final GitRepo gitRepo) {
        return gitRepo.getGitRepoMembers().stream()
                .map(gitRepoMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void saveGitRepo(final String name) {
        gitRepoRepository.save(gitRepoMapper.toEntity(name));
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompareAndUpdate(final GitRepoCompareRequest gitRepoCompareRequest) {
        return getTwoGitRepoMemberResponse(gitRepoCompareRequest,
                LocalDate.now().getYear(), memberService.getLoginUserWithPersistence().getGithubToken());
    }

    private TwoGitRepoMemberResponse getTwoGitRepoMemberResponse(final GitRepoCompareRequest gitRepoCompareRequest, final Integer year, final String githubToken) {
        return new TwoGitRepoMemberResponse(getGitRepoMemberResponses(gitRepoCompareRequest.getFirstRepo(), year, githubToken),
                getGitRepoMemberResponses(gitRepoCompareRequest.getSecondRepo(), year, githubToken));
    }

    private List<GitRepoMemberResponse> getGitRepoMemberResponses(final String repo, final Integer year, final String githubToken) {
        requestKafkaIssue(new GitRepoNameRequest(repo));
        return findMembersByGitRepoWithClient(new GitRepoInfoRequest(githubToken, repo, year));
    }

    public GitRepoMemberCompareResponse findTwoGitRepoMember(final GitRepoMemberCompareRequest gitRepoMemberCompareRequest) {
        GitRepoMember firstGitRepoMember =
                gitRepoMemberService.findByNameAndMemberGithubId(gitRepoMemberCompareRequest.getFirstRepo(), gitRepoMemberCompareRequest.getFirstGithubId());
        GitRepoMember secondGitRepoMember =
                gitRepoMemberService.findByNameAndMemberGithubId(gitRepoMemberCompareRequest.getSecondRepo(), gitRepoMemberCompareRequest.getSecondGithubId());

        return new GitRepoMemberCompareResponse(
                gitRepoMemberMapper.toResponse(firstGitRepoMember),
                gitRepoMemberMapper.toResponse(secondGitRepoMember));
    }

    public TwoGitRepoResponse findTwoGitReposAndUpdate(final GitRepoCompareRequest twoGitRepoCompareRequest) {
        return new TwoGitRepoResponse(getOneRepoResponse(twoGitRepoCompareRequest.getFirstRepo()),
                getOneRepoResponse(twoGitRepoCompareRequest.getSecondRepo()));
    }

    public void updateClosedIssues(final ClosedIssueKafkaResponse closedIssueKafkaResponse) {
        GitRepo gitRepo = gitRepoRepository.findByName(closedIssueKafkaResponse.getName())
                .orElseThrow(EntityNotFoundException::new);

        gitRepo.updateClosedIssueNum(closedIssueKafkaResponse.getClosedIssue());
    }

    public GitRepoCompareResponse getOneRepoResponse(final String repoName) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        GitRepoClientResponse repoResponse = requestClientGitRepo(repoName, githubToken);

        repoResponse.setClosed_issues_count(getEntityByName(repoName).getClosedIssueNum());
        requestKafkaIssue(new GitRepoNameRequest(repoName));

        return getGitRepoResponse(repoName, repoResponse, requestClientGitRepoLanguage(repoName, githubToken));
    }

    private GitRepoClientResponse requestClientGitRepo(final String repoName, final String githubToken) {
        return gitRepoClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));
    }

    private GitRepoCompareResponse getGitRepoResponse(final String repoName, final GitRepoClientResponse repoResponse, final GitRepoLanguageMap gitRepoLanguageMap) {
        return new GitRepoCompareResponse(repoResponse, getStatistics(repoName), gitRepoLanguageMap.getLanguages(), gitRepoLanguageMap.getStatistics());
    }

    public GitRepoLanguageMap requestClientGitRepoLanguage(final String repoName, final String githubToken) {
        return new GitRepoLanguageMap(gitRepoLanguageClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName)));
    }

    public GitRepo findGitRepo(final String repoName) {
        return gitRepoRepository.findByName(repoName).orElse(gitRepoRepository.save(new GitRepo(repoName)));
    }

    private StatisticsResponse getStatistics(final String name) {
        GitRepo gitRepo = getEntityByName(name);
        return getContributionStatisticsResponse(gitRepo.getGitRepoMembers());
    }

    private StatisticsResponse getContributionStatisticsResponse(final Set<GitRepoMember> gitRepoMembers) {
        return getStatisticsResponse(
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getCommits()),
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getAdditions()),
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getDeletions()));
    }

    private List<Integer> getContributionList(final Set<GitRepoMember> gitRepoMembers, final Function<GitRepoMember, Integer> function) {
        return gitRepoMembers.stream().map(function).collect(Collectors.toList());
    }

    private StatisticsResponse getStatisticsResponse(final List<Integer> commits, final List<Integer> additions, final List<Integer> deletions) {
        return new StatisticsResponse(
                commits.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : commits.stream().mapToInt(Integer::intValue).summaryStatistics(),
                additions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : additions.stream().mapToInt(Integer::intValue).summaryStatistics(),
                deletions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : deletions.stream().mapToInt(Integer::intValue).summaryStatistics());
    }

    public List<GitRepoMemberResponse> requestToGithub(final GitRepoInfoRequest gitRepoInfoRequest) {
        List<GitRepoMemberClientResponse> contributions = Arrays.asList(requestClientGitRepoMember(gitRepoInfoRequest));
        if (contributions.isEmpty()) return List.of();

        List<GitRepoMemberResponse> result = getResponseList(
                contributions, getContributionMap(contributions, Week::getA), getContributionMap(contributions, Week::getD));

        gitRepoMemberService.saveAll(result, gitRepoInfoRequest.getName());
        return result;
    }

    public GitRepoMemberClientResponse[] requestClientGitRepoMember(final GitRepoInfoRequest gitRepoInfoRequest) {
        return gitRepoMemberClient.requestToGithub(gitRepoInfoRequest);
    }

    private List<GitRepoMemberResponse> getResponseList(
            final List<GitRepoMemberClientResponse> contributions,
            final GitRepoContributionMap additions,
            final GitRepoContributionMap deletions) {

        return contributions.stream()
                .map(clientResponse -> {
                    String githubId = clientResponse.getAuthor().getLogin();
                    return new GitRepoMemberResponse(
                            githubId,
                            clientResponse.getTotal(),
                            additions.getContributionByKey(clientResponse),
                            deletions.getContributionByKey(clientResponse),
                            memberService.findMemberOrSave(new MemberRequest(githubId), AuthStep.NONE).isServiceMember());
                }).collect(Collectors.toList());
    }

    private GitRepoContributionMap getContributionMap(final List<GitRepoMemberClientResponse> contributions, final ToIntFunction<Week> function) {
        return new GitRepoContributionMap(contributions.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> Arrays.stream(mem.getWeeks()).mapToInt(function).sum())));
    }

    private GitRepo getEntityByName(final String name) {
        return gitRepoRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    private void requestKafkaIssue(final GitRepoNameRequest gitRepoNameRequest) {
        kafkaIssueProducer.send(gitRepoNameRequest);
    }

    @Override
    public GitRepo loadEntity(final Long id) {
        return gitRepoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public GitRepoResponse findGitRepoInfos(final String name) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        List<GitRepoMemberResponse> gitRepoMemberResponses = getGitRepoMemberResponses(name, githubToken);

        return new GitRepoResponse(updateAndGetSparkLine(githubToken, name, getOrSaveGitRepo(name)), gitRepoMemberResponses);
    }

    public List<GitRepoMemberResponse> getGitRepoMemberResponses(final String name, final String githubToken) {
        GitRepo gitRepo = findGitRepo(name);
        Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
        if (!gitRepoMembers.isEmpty()) {
            return gitRepoMapper.toGitRepoMemberResponseList(gitRepoMembers);
        }
        return requestToGithub(new GitRepoInfoRequest(githubToken, name, LocalDate.now().getYear()));
    }

    public TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        String firstRepo = request.getFirstRepo();
        String secondRepo = request.getSecondRepo();

        return new TwoGitRepoResponse(
                getGitRepoResponse(
                        firstRepo,
                        requestClientGitRepo(githubToken, firstRepo),
                        requestClientGitRepoLanguage(firstRepo, githubToken)),
                getGitRepoResponse(
                        secondRepo,
                        requestClientGitRepo(githubToken, secondRepo),
                        requestClientGitRepoLanguage(secondRepo, githubToken)));
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(final GitRepoCompareRequest request) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        return new TwoGitRepoMemberResponse(
                getGitRepoMemberResponses(request.getFirstRepo(), githubToken),
                getGitRepoMemberResponses(request.getSecondRepo(), githubToken));
    }
}
