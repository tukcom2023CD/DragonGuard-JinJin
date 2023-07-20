package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.*;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoContributionMap;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoLanguageMap;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.StatisticsResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.SummaryResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.client.GithubClient;
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
public class GitRepoServiceImpl implements EntityLoader<GitRepo, Long>, GitRepoService {
    private final GitRepoRepository gitRepoRepository;
    private final AuthService authService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaProducer<SparkLineKafka> kafkaSparkLineProducer;
    private final KafkaProducer<GitRepoRequest> kafkaGitRepoInfoProducer;
    private final GithubClient<GitRepoInfoRequest, List<GitRepoMemberClientResponse>> gitRepoMemberClient;
    private final GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    private final GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;
    private final GithubClient<GitRepoClientRequest, GitRepoSparkLineResponse> gitRepoSparkLineClient;
    private final GithubClient<GitRepoClientRequest, GitRepoIssueResponse[]> gitRepoIssueClient;

    public List<Integer> updateAndGetSparkLine(final String name, final String githubToken, final GitRepo gitRepo) {
        List<Integer> savedSparkLine = gitRepo.getSparkLine();
        if (!savedSparkLine.isEmpty()) {
            requestKafkaSparkLine(githubToken, gitRepo.getId());
            return savedSparkLine;
        }
        List<Integer> sparkLine = requestClientSparkLine(githubToken, name);
        gitRepo.updateSparkLine(sparkLine);
        return sparkLine;
    }

    public void updateSparkLine(final Long id, final String githubToken) {
        GitRepo gitRepo = loadEntity(id);
        gitRepo.updateSparkLine(requestClientSparkLine(githubToken, gitRepo.getName()));
    }

    private List<Integer> requestClientSparkLine(final String githubToken, final String name) {
        return Arrays.asList(gitRepoSparkLineClient.requestToGithub(new GitRepoClientRequest(githubToken, name)).getAll());
    }

    @Override
    public TwoGitRepoResponse findTwoGitReposAndUpdate(final GitRepoCompareRequest twoGitRepoCompareRequest) {
        return new TwoGitRepoResponse(getOneRepoResponse(twoGitRepoCompareRequest.getFirstRepo()),
                getOneRepoResponse(twoGitRepoCompareRequest.getSecondRepo()));
    }

    private GitRepoCompareResponse getOneRepoResponse(final String repoName) {
        String githubToken = authService.getLoginUser().getGithubToken();
        GitRepoClientResponse repoResponse = requestClientGitRepo(repoName, githubToken);

        repoResponse.setClosedIssuesCount(requestClientGitRepoIssue(repoName, githubToken));

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

    private Integer requestClientGitRepoIssue(final String repoName, final String githubToken) {
        return gitRepoIssueClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName)).length;
    }

    public GitRepo findGitRepo(final String repoName) {
        if (gitRepoRepository.existsByName(repoName)) {
            return gitRepoRepository.findByName(repoName).orElseThrow(EntityNotFoundException::new);
        }
        return gitRepoRepository.save(gitRepoMapper.toEntity(repoName));
    }

    private StatisticsResponse getStatistics(final GitRepo gitRepo) {
        Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
        if (isContributionEmpty(gitRepoMembers)) {
            requestKafkaGitRepoInfo(authService.getLoginUser().getGithubToken(), gitRepo.getName());
            return getStatisticsResponse(List.of(), List.of(), List.of());
        }
        return getStatisticsResponse(
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getCommits()),
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getAdditions()),
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getDeletions()));
    }

    private boolean isContributionEmpty(final Set<GitRepoMember> gitRepoMembers) {
        return gitRepoMembers.stream().anyMatch(grm -> grm.getGitRepoContribution() == null
                || grm.getGitRepoContribution().getCommits() == null);
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

    public Optional<List<GitRepoMemberClientResponse>> requestClientGitRepoMember(final GitRepoInfoRequest gitRepoInfoRequest) {
        List<GitRepoMemberClientResponse> responses = gitRepoMemberClient.requestToGithub(gitRepoInfoRequest);
        if (responses == null || responses.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(responses);
    }

    public GitRepoContributionMap getContributionMap(final Set<GitRepoMemberClientResponse> contributions, final ToIntFunction<Week> function) {
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

    private void requestKafkaSparkLine(final String githubToken, final Long id) {
        kafkaSparkLineProducer.send(new SparkLineKafka(githubToken, id));
    }

    public void requestKafkaGitRepoInfo(final String githubToken, final String name) {
        kafkaGitRepoInfoProducer.send(new GitRepoRequest(githubToken, name, LocalDate.now().getYear()));
    }

    @Override
    public GitRepo loadEntity(final Long id) {
        return gitRepoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Cacheable(value = "twoGitRepos", key = "{#request.firstRepo, #request.secondRepo}", cacheManager = "cacheManager",
            unless = "#result.firstRepo.getProfileUrls().?[#this == null].size() > 0 " +
                    "|| #result.secondRepo.getProfileUrls().?[#this == null].size() > 0 " +
                    "|| #result.firstRepo.statistics.additionStats.max == 0 " +
                    "|| #result.secondRepo.statistics.additionStats.max == 0")
    public TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request) {
        String githubToken = authService.getLoginUser().getGithubToken();
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

    public boolean gitRepoExistsByName(final String name) {
        return gitRepoRepository.existsByName(name);
    }

    public void saveAll(final Set<GitRepo> gitRepos) {
        gitRepoRepository.saveAll(gitRepos);
    }
}
