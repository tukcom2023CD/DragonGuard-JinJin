package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.ClosedIssueKafkaResponse;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.StatisticsResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepoLanguage;
import com.dragonguard.backend.domain.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.Week;
import com.dragonguard.backend.domain.gitrepomember.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.KafkaProducer;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
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
    private final AuthService authService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaProducer<GitRepoNameRequest> kafkaIssueProducer;
    private final GithubClient<GitRepoRequest, GitRepoMemberClientResponse[]> gitRepoMemberClient;
    private final GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    private final GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;

    public List<GitRepoMemberResponse> findMembersByGitRepoWithClient(final GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = gitRepoRepository.findByName(gitRepoRequest.getName());
        if (!StringUtils.hasText(gitRepoRequest.getGithubToken())) {
            gitRepoRequest.setGithubToken(authService.getLoginUser().getGithubToken());
        }
        if (gitRepo.isEmpty()) {
            gitRepoRepository.save(gitRepoMapper.toEntity(gitRepoRequest));
            return requestToGithub(gitRepoRequest);
        }
        if (gitRepo.get().getGitRepoMembers().isEmpty() || gitRepo.get().getGitRepoMembers().stream().findFirst().get().getGitRepoContribution() == null) {
            return requestToGithub(gitRepoRequest);
        }
        return gitRepoMemberService.findAllByGitRepo(gitRepo.get()).stream()
                .map(gitRepoMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(final GitRepoCompareRequest gitRepoCompareRequest) {
        Integer year = LocalDate.now().getYear();
        String githubToken = authService.getLoginUser().getGithubToken();
        String firstRepo = gitRepoCompareRequest.getFirstRepo();
        String secondRepo = gitRepoCompareRequest.getSecondRepo();

        List<GitRepoMemberResponse> firstResult = findMembersByGitRepoWithClient(new GitRepoRequest(githubToken, firstRepo, year));
        List<GitRepoMemberResponse> secondResult = findMembersByGitRepoWithClient(new GitRepoRequest(githubToken, secondRepo, year));

        requestIssueToScraping(new GitRepoNameRequest(firstRepo));
        requestIssueToScraping(new GitRepoNameRequest(secondRepo));

        return new TwoGitRepoMemberResponse(firstResult, secondResult);
    }

    public GitRepoMemberCompareResponse findTwoGitRepoMember(final GitRepoMemberCompareRequest gitRepoMemberCompareRequest) {
        GitRepoMember first = gitRepoMemberService.findByNameAndMemberName(gitRepoMemberCompareRequest.getFirstRepo(), gitRepoMemberCompareRequest.getFirstName());
        GitRepoMember second = gitRepoMemberService.findByNameAndMemberName(gitRepoMemberCompareRequest.getSecondRepo(), gitRepoMemberCompareRequest.getSecondName());

        return new GitRepoMemberCompareResponse(gitRepoMemberMapper.toResponse(first), gitRepoMemberMapper.toResponse(second));
    }

    public TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest twoGitRepoCompareRequest) {
        return new TwoGitRepoResponse(getOneRepoResponse(twoGitRepoCompareRequest.getFirstRepo()), getOneRepoResponse(twoGitRepoCompareRequest.getSecondRepo()));
    }

    public void updateClosedIssues(final ClosedIssueKafkaResponse closedIssueKafkaResponse) {
        GitRepo gitRepo = gitRepoRepository.findByName(closedIssueKafkaResponse.getName())
                .orElseThrow(EntityNotFoundException::new);

        gitRepo.updateClosedIssueNum(closedIssueKafkaResponse.getClosedIssue());
    }

    private GitRepoResponse getOneRepoResponse(final String repoName) {
        Integer year = LocalDate.now().getYear();
        String githubToken = authService.getLoginUser().getGithubToken();
        GitRepo repo = gitRepoRepository.findByName(repoName).orElseGet(() -> gitRepoRepository.save(gitRepoMapper.toEntity(new GitRepoRequest(repoName, year))));
        GitRepoClientResponse repoResponse = gitRepoClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));
        if (repo.getClosedIssueNum() != null) {
            repoResponse.setClosed_issues_count(repo.getClosedIssueNum());
        } else {
            requestIssueToScraping(new GitRepoNameRequest(repoName));
        }
        GitRepoLanguage gitRepoLanguage = new GitRepoLanguage(gitRepoLanguageClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName)));

        IntSummaryStatistics langStats = gitRepoLanguage.getStatistics();

        return new GitRepoResponse(repoResponse, getStatistics(repoName), gitRepoLanguage.getLanguages(), langStats);
    }

    private StatisticsResponse getStatistics(final String name) {
        GitRepo gitRepo = getEntityByName(name);
        List<Integer> commits = gitRepo.getGitRepoMembers().stream().map(g -> g.getGitRepoContribution().getCommits()).collect(Collectors.toList());
        List<Integer> additions = gitRepo.getGitRepoMembers().stream().map(g -> g.getGitRepoContribution().getAdditions()).collect(Collectors.toList());
        List<Integer> deletions = gitRepo.getGitRepoMembers().stream().map(g -> g.getGitRepoContribution().getDeletions()).collect(Collectors.toList());

        return new StatisticsResponse(
                commits.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : commits.stream().mapToInt(Integer::intValue).summaryStatistics(),
                additions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : additions.stream().mapToInt(Integer::intValue).summaryStatistics(),
                deletions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : deletions.stream().mapToInt(Integer::intValue).summaryStatistics());
    }

    private GitRepo getEntityByName(final String name) {
        return gitRepoRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    private void requestIssueToScraping(final GitRepoNameRequest gitRepoNameRequest) {
        kafkaIssueProducer.send(gitRepoNameRequest);
    }

    private List<GitRepoMemberResponse> requestToGithub(final GitRepoRequest gitRepoRequest) {
        GitRepoMemberClientResponse[] clientResponse = gitRepoMemberClient.requestToGithub(gitRepoRequest);
        if (clientResponse == null || clientResponse.length == 0) {
            return List.of();
        }

        List<GitRepoMemberClientResponse> gitRepoClientResponse = Arrays.asList(clientResponse);

        Map<GitRepoMemberClientResponse, Integer> additions = gitRepoClientResponse.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> Arrays.stream(mem.getWeeks()).mapToInt(Week::getA).sum()));

        Map<GitRepoMemberClientResponse, Integer> deletions = gitRepoClientResponse.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> Arrays.stream(mem.getWeeks()).mapToInt(Week::getD).sum()));

        List<GitRepoMemberResponse> result = gitRepoClientResponse.stream()
                .map(member -> new GitRepoMemberResponse(
                        member.getAuthor().getLogin(),
                        member.getTotal(),
                        additions.get(member),
                        deletions.get(member))).collect(Collectors.toList());

        gitRepoMemberService.saveAll(result, gitRepoRequest.getName());

        return result;
    }

    @Override
    public GitRepo loadEntity(final Long id) {
        return gitRepoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
