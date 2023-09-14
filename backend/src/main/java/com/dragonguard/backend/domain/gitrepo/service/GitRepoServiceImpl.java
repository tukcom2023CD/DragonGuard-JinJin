package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.*;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoContributions;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoLanguages;
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
public class GitRepoServiceImpl implements GitRepoService {
    private static final int NO_CONTRIBUTION = 0;
    private final GitRepoRepository gitRepoRepository;
    private final AuthService authService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaProducer<SparkLineKafka> kafkaSparkLineProducer;
    private final KafkaProducer<GitRepoRequest> kafkaGitRepoInfoProducer;
    private final GithubClient<GitRepoInfoRequest, List<GitRepoMemberClientResponse>> gitRepoMemberClient;
    private final GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    private final GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;
    private final GithubClient<GitRepoClientRequest, GitRepoSparkLineResponse> gitRepoSparkLineClient;
    private final GithubClient<GitRepoClientRequest, Integer> gitRepoIssueClient;

    @Override
    public List<Integer> updateAndGetSparkLine(final String name, final String githubToken, final GitRepo gitRepo) {
        final List<Integer> savedSparkLine = gitRepo.getSparkLine();
        if (!savedSparkLine.isEmpty()) {
            requestKafkaSparkLine(githubToken, gitRepo.getId());
            return savedSparkLine;
        }
        final List<Integer> sparkLine = requestClientSparkLine(githubToken, name);
        gitRepo.updateSparkLine(sparkLine);
        return sparkLine;
    }

    public void updateSparkLine(final Long id, final String githubToken) {
        final GitRepo gitRepo = loadEntity(id);
        gitRepo.updateSparkLine(requestClientSparkLine(githubToken, gitRepo.getName()));
    }

    private List<Integer> requestClientSparkLine(final String githubToken, final String name) {
        return Arrays.asList(gitRepoSparkLineClient.requestToGithub(new GitRepoClientRequest(githubToken, name)).getAll());
    }

    @Override
    public TwoGitRepoResponse findTwoGitReposAndUpdate(final GitRepoCompareRequest twoGitRepoCompareRequest) {
        return new TwoGitRepoResponse(findOneRepoResponse(twoGitRepoCompareRequest.getFirstRepo()),
                findOneRepoResponse(twoGitRepoCompareRequest.getSecondRepo()));
    }

    private GitRepoCompareResponse findOneRepoResponse(final String repoName) {
        final String githubToken = authService.getLoginUser().getGithubToken();
        final GitRepoClientResponse repoResponse = requestClientGitRepo(repoName, githubToken);

        repoResponse.setClosedIssuesCount(requestClientGitRepoIssue(repoName, githubToken));

        return findGitRepoResponse(repoName, repoResponse, requestClientGitRepoLanguage(repoName, githubToken), githubToken);
    }

    private GitRepoClientResponse requestClientGitRepo(final String repoName, final String githubToken) {
        return gitRepoClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));
    }

    private GitRepoCompareResponse findGitRepoResponse(
            final String repoName,
            final GitRepoClientResponse repoResponse,
            final GitRepoLanguages gitRepoLanguages,
            final String githubToken) {
        final GitRepo gitRepo = findGitRepo(repoName);
        final Set<GitRepoMember> gitRepoMembers = gitRepo.getGitRepoMembers();
        repoResponse.setClosedIssuesCount(requestClientGitRepoIssue(repoName, githubToken));

        return new GitRepoCompareResponse(
                repoResponse,
                getStatistics(gitRepo),
                gitRepoLanguages.getLanguages(),
                new SummaryResponse(gitRepoLanguages.getStatistics()),
                getProfileUrls(gitRepoMembers));
    }

    private List<String> getProfileUrls(final Set<GitRepoMember> gitRepoMembers) {
        return gitRepoMembers.stream()
                .map(gitRepoMember -> gitRepoMember.getMember().getProfileImage())
                .collect(Collectors.toList());
    }

    private GitRepoLanguages requestClientGitRepoLanguage(final String repoName, final String githubToken) {
        return new GitRepoLanguages(gitRepoLanguageClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName)));
    }

    private Integer requestClientGitRepoIssue(final String repoName, final String githubToken) {
        return gitRepoIssueClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));
    }

    @Override
    public GitRepo findGitRepo(final String repoName) {
        return gitRepoRepository.findByName(repoName).orElseGet(() -> gitRepoRepository.save(gitRepoMapper.toEntity(repoName)));
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
        return new StatisticsResponse(getSummaryResponse(commits), getSummaryResponse(additions), getSummaryResponse(deletions));
    }

    private SummaryResponse getSummaryResponse(final List<Integer> contributions) {
        if (contributions.isEmpty()) {
            return new SummaryResponse(new IntSummaryStatistics(NO_CONTRIBUTION, NO_CONTRIBUTION, NO_CONTRIBUTION, NO_CONTRIBUTION));
        }
        return new SummaryResponse(contributions.stream().mapToInt(Integer::intValue).summaryStatistics());
    }

    @Override
    public Optional<List<GitRepoMemberClientResponse>> requestClientGitRepoMember(final GitRepoInfoRequest gitRepoInfoRequest) {
        List<GitRepoMemberClientResponse> responses = gitRepoMemberClient.requestToGithub(gitRepoInfoRequest);
        if (responses == null || responses.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(responses);
    }

    @Override
    public GitRepoContributions getContributionMap(final Set<GitRepoMemberClientResponse> contributions, final ToIntFunction<Week> function) {
        if (hasNoContribution(contributions)) {
            return null;
        }
        return new GitRepoContributions(contributions.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> mem.getWeeks().stream().mapToInt(function).sum())));
    }

    private boolean hasNoContribution(final Set<GitRepoMemberClientResponse> contributions) {
        return contributions == null || contributions.isEmpty()
                || contributions.stream().map(GitRepoMemberClientResponse::getWeeks).filter(Objects::nonNull).findFirst().isEmpty();
    }

    private void requestKafkaSparkLine(final String githubToken, final Long id) {
        kafkaSparkLineProducer.send(new SparkLineKafka(githubToken, id));
    }

    @Override
    public void requestKafkaGitRepoInfo(final String githubToken, final String name) {
        kafkaGitRepoInfoProducer.send(new GitRepoRequest(githubToken, name, LocalDate.now().getYear()));
    }

    @Override
    public GitRepo loadEntity(final Long id) {
        return gitRepoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request) {
        String githubToken = authService.getLoginUser().getGithubToken();
        String firstRepo = request.getFirstRepo();
        String secondRepo = request.getSecondRepo();

        return new TwoGitRepoResponse(
                findGitRepoResponse(
                        firstRepo,
                        requestClientGitRepo(firstRepo, githubToken),
                        requestClientGitRepoLanguage(firstRepo, githubToken),
                        githubToken),
                findGitRepoResponse(
                        secondRepo,
                        requestClientGitRepo(secondRepo, githubToken),
                        requestClientGitRepoLanguage(secondRepo, githubToken),
                        githubToken));
    }

    public boolean gitRepoExistsByName(final String name) {
        return gitRepoRepository.existsByName(name);
    }

    @Override
    public void saveAll(final Set<GitRepo> gitRepos) {
        gitRepoRepository.saveAll(gitRepos);
    }
}
