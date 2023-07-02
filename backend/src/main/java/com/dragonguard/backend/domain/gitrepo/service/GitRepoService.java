package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoSparkLineResponse;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.ClosedIssueKafkaResponse;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.*;
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
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

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
    private final KafkaProducer<SparkLineKafka> kafkaSparkLineProducer;
    private final KafkaProducer<GitRepoRequest> kafkaGitRepoInfoProducer;
    private final GithubClient<GitRepoInfoRequest, List<GitRepoMemberClientResponse>> gitRepoMemberClient;
    private final GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    private final GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;
    private final GithubClient<GitRepoClientRequest, GitRepoSparkLineResponse> gitRepoSparkLineClient;

    public GitRepoResponse findGitRepoInfosAndUpdate(final String name) {
        if (gitRepoRepository.existsByName(name)) {
            String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
            GitRepo gitRepo = findGitRepo(name);
            return new GitRepoResponse(
                    updateAndGetSparkLine(name, githubToken, gitRepo),
                    getGitRepoMemberResponses(gitRepo, githubToken));
        }

        int year = LocalDate.now().getYear();
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();

        return new GitRepoResponse(
                updateAndGetSparkLine(name, githubToken, findGitRepo(name)),
                getGitRepoMemberResponses(name, year, githubToken));
    }

    public List<Integer> updateAndGetSparkLine(final String name, final String githubToken, final GitRepo gitRepo) {
        List<Integer> savedSparkLine = gitRepo.getSparkLine();
        if (!savedSparkLine.isEmpty()) {
            requestKafkaSparkLine(githubToken, gitRepo.getId());
            return savedSparkLine;
        }
        List<Integer> sparkLine = Arrays.asList(requestClientSparkLine(githubToken, name).getAll());
        gitRepo.updateSparkLine(sparkLine);
        return sparkLine;
    }

    public void updateSparkLine(final Long id, final String githubToken) {
        GitRepo gitRepo = loadEntity(id);
        gitRepo.updateSparkLine(Arrays.asList(requestClientSparkLine(githubToken, gitRepo.getName()).getAll()));
    }

    private GitRepoSparkLineResponse requestClientSparkLine(final String githubToken, final String name) {
        return gitRepoSparkLineClient.requestToGithub(new GitRepoClientRequest(githubToken, name));
    }

    private List<GitRepoMemberResponse> findMembersByGitRepoWithClient(final GitRepoInfoRequest gitRepoInfoRequest) {
        GitRepo gitRepo = findGitRepo(gitRepoInfoRequest.getName());

        return requestToGithub(gitRepoInfoRequest, gitRepo);
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
        String name = closedIssueKafkaResponse.getName();
        if (!gitRepoRepository.existsByName(name)) return;

        gitRepoRepository.findByName(name)
                .orElseThrow(EntityNotFoundException::new)
                .updateClosedIssueNum(closedIssueKafkaResponse.getClosedIssue());
    }

    private GitRepoCompareResponse getOneRepoResponse(final String repoName) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        GitRepoClientResponse repoResponse = requestClientGitRepo(repoName, githubToken);

        repoResponse.setClosedIssuesCount(getEntityByName(repoName).getClosedIssueNum());
        requestKafkaIssue(new GitRepoNameRequest(repoName));

        return getGitRepoResponse(repoName, repoResponse, requestClientGitRepoLanguage(repoName, githubToken));
    }

    private GitRepoClientResponse requestClientGitRepo(final String repoName, final String githubToken) {
        return gitRepoClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));
    }

    private GitRepoCompareResponse getGitRepoResponse(final String repoName, final GitRepoClientResponse repoResponse, final GitRepoLanguageMap gitRepoLanguageMap) {
        GitRepo gitRepo = getEntityByName(repoName);
        if (gitRepo == null) return null;

        Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
        if (gitRepoMembers.isEmpty()) return null;

        List<String> profileUrls = gitRepoMembers.stream()
                .map(gitRepoMember -> gitRepoMember.getMember().getProfileImage())
                .collect(Collectors.toList());

        return new GitRepoCompareResponse(
                repoResponse,
                getStatistics(gitRepo),
                gitRepoLanguageMap.getLanguages(),
                new SummaryResponse(gitRepoLanguageMap.getStatistics()),
                profileUrls);
    }

    private GitRepoLanguageMap requestClientGitRepoLanguage(final String repoName, final String githubToken) {
        return new GitRepoLanguageMap(gitRepoLanguageClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName)));
    }

    private GitRepo findGitRepo(final String repoName) {
        if (gitRepoRepository.existsByName(repoName)) {
            requestKafkaIssue(new GitRepoNameRequest(repoName));
            return gitRepoRepository.findByName(repoName).orElseThrow(EntityNotFoundException::new);
        }
        GitRepo gitRepo = gitRepoRepository.save(gitRepoMapper.toEntity(repoName));
        requestKafkaIssue(new GitRepoNameRequest(repoName));
        return loadEntity(gitRepo.getId());
    }

    private StatisticsResponse getStatistics(final GitRepo gitRepo) {
        Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
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
                commits.isEmpty() ? new SummaryResponse(new IntSummaryStatistics(0, 0, 0, 0)) : new SummaryResponse(commits.stream().mapToInt(Integer::intValue).summaryStatistics()),
                additions.isEmpty() ? new SummaryResponse(new IntSummaryStatistics(0, 0, 0, 0)) : new SummaryResponse(additions.stream().mapToInt(Integer::intValue).summaryStatistics()),
                deletions.isEmpty() ? new SummaryResponse(new IntSummaryStatistics(0, 0, 0, 0)) : new SummaryResponse(deletions.stream().mapToInt(Integer::intValue).summaryStatistics()));
    }

    public List<GitRepoMemberResponse> requestToGithub(final GitRepoInfoRequest gitRepoInfoRequest, final GitRepo gitRepo) {
        Optional<List<GitRepoMemberClientResponse>> responses = requestClientGitRepoMember(gitRepoInfoRequest);
        if (responses.isEmpty()) {
            return List.of();
        }
        Set<GitRepoMemberClientResponse> contributions = new HashSet<>(responses.get());
        if (contributions.isEmpty()) return List.of();

        List<GitRepoMemberResponse> result = getResponseList(
                contributions,
                getContributionMap(contributions, Week::getA),
                getContributionMap(contributions, Week::getD));

        if (result.isEmpty()) return List.of();

        gitRepoMemberService.saveAll(result, gitRepo);
        return result;
    }

    private Optional<List<GitRepoMemberClientResponse>> requestClientGitRepoMember(final GitRepoInfoRequest gitRepoInfoRequest) {
        List<GitRepoMemberClientResponse> responses = gitRepoMemberClient.requestToGithub(gitRepoInfoRequest);
        if (responses == null || responses.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(responses);
    }

    private List<GitRepoMemberResponse> getResponseList(
            final Set<GitRepoMemberClientResponse> contributions,
            final GitRepoContributionMap additions,
            final GitRepoContributionMap deletions) {

        return contributions.stream()
                .filter(c -> c.getWeeks() != null && !c.getWeeks().isEmpty() && c.getTotal() != null && c.getAuthor() != null)
                .map(clientResponse -> {
                    String githubId = clientResponse.getAuthor().getLogin();
                    String profileUrl = clientResponse.getAuthor().getAvatarUrl();
                    return new GitRepoMemberResponse(
                            githubId,
                            profileUrl,
                            clientResponse.getTotal(),
                            additions.getContributionByKey(clientResponse),
                            deletions.getContributionByKey(clientResponse),
                            memberService.findMemberOrSave(githubId, AuthStep.NONE, profileUrl).isServiceMember());
                }).collect(Collectors.toList());
    }

    private GitRepoContributionMap getContributionMap(final Set<GitRepoMemberClientResponse> contributions, final ToIntFunction<Week> function) {
        if (contributions == null || contributions.isEmpty()
                || contributions.stream().map(GitRepoMemberClientResponse::getWeeks).filter(Objects::nonNull).findFirst().isEmpty()) return null;
        return new GitRepoContributionMap(contributions.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> mem.getWeeks().stream().mapToInt(function).sum())));
    }

    public GitRepo getEntityByName(final String name) {
        if (!gitRepoRepository.existsByName(name)) {
            return null;
        }
        return gitRepoRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    private void requestKafkaIssue(final GitRepoNameRequest gitRepoNameRequest) {
        kafkaIssueProducer.send(gitRepoNameRequest);
    }

    private void requestKafkaSparkLine(final String githubToken, final Long id) {
        kafkaSparkLineProducer.send(new SparkLineKafka(githubToken, id));
    }

    private void requestKafkaGitRepoInfo(String githubToken, String name) {
        kafkaGitRepoInfoProducer.send(new GitRepoRequest(githubToken, name, LocalDate.now().getYear()));
    }

    @Override
    public GitRepo loadEntity(final Long id) {
        return gitRepoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public GitRepoResponse findGitRepoInfos(final String name) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        GitRepo gitRepo = findGitRepo(name);

        return new GitRepoResponse(
                updateAndGetSparkLine(name, githubToken, gitRepo),
                getGitRepoMemberResponses(gitRepo, githubToken));
    }

    private List<GitRepoMemberResponse> getGitRepoMemberResponses(final GitRepo gitRepo, final String githubToken) {
        Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
        if (!gitRepoMembers.isEmpty()) {
            requestKafkaGitRepoInfo(githubToken, gitRepo.getName());
            return gitRepoMemberMapper.toResponseList(gitRepoMembers);
        }
        return requestToGithub(new GitRepoInfoRequest(githubToken, gitRepo.getName(), LocalDate.now().getYear()), gitRepo);
    }

    @Cacheable(value = "twoGitRepos", key = "{#request.firstRepo, #request.secondRepo}", cacheManager = "cacheManager", unless = "#result.firstRepo.getProfileUrls().?[#this == null].size() > 0 || #result.secondRepo.getProfileUrls().?[#this == null].size() > 0")
    public TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        String firstRepo = request.getFirstRepo();
        String secondRepo = request.getSecondRepo();

        return new TwoGitRepoResponse(
                getGitRepoResponse(
                        firstRepo,
                        requestClientGitRepo(firstRepo, githubToken),
                        requestClientGitRepoLanguage(firstRepo, githubToken)),
                getGitRepoResponse(
                        secondRepo,
                        requestClientGitRepo(secondRepo, githubToken),
                        requestClientGitRepoLanguage(secondRepo, githubToken)));
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(final GitRepoCompareRequest request) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        return new TwoGitRepoMemberResponse(
                getGitRepoMemberResponses(findGitRepo(request.getFirstRepo()), githubToken),
                getGitRepoMemberResponses(findGitRepo(request.getSecondRepo()), githubToken));
    }
}
