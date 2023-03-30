package com.dragonguard.backend.gitrepo.service;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoClientRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.dto.response.*;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberClientResponse;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.util.KafkaProducer;
import com.dragonguard.backend.util.GithubClient;
import com.dragonguard.backend.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 서비스 로직을 처리하는 클래스
 */

@Service
@RequiredArgsConstructor
public class GitRepoService {
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberService gitRepoMemberService;
    private final AuthService authService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaProducer<GitRepoRequest> kafkaGitRepoProducer;
    private final KafkaProducer<GitRepoNameRequest> kafkaIssueProducer;
    private final GithubClient<GitRepoRequest, GitRepoMemberClientResponse[]> gitRepoMemberClient;
    private final GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    private final GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;

    public List<GitRepoMemberResponse> findMembersByGitRepo(GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = gitRepoRepository.findByName(gitRepoRequest.getName());
        if (gitRepo.isEmpty()) {
            gitRepoRepository.save(gitRepoMapper.toEntity(gitRepoRequest));
            requestGitRepoToScraping(gitRepoRequest);
            return List.of();
        } else if (gitRepo.get().getGitRepoMembers().isEmpty()) {
            requestGitRepoToScraping(gitRepoRequest);
            return List.of();
        }
        return gitRepoMemberService.findAllByGitRepo(gitRepo.get()).stream()
                .map(gitRepoMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "gitRepoMemberResponseList", key = "#gitRepoRequest", cacheManager = "cacheManager", unless = "#result.size() == 0")
    public List<GitRepoMemberResponse> findMembersByGitRepoWithClient(GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = gitRepoRepository.findByName(gitRepoRequest.getName());
        if (!StringUtils.hasText(gitRepoRequest.getGithubToken())) {
            gitRepoRequest.setGithubToken(authService.getLoginUser().getGithubToken());
        }
        if (gitRepo.isEmpty()) {
            gitRepoRepository.save(gitRepoMapper.toEntity(gitRepoRequest));
            return requestToGithub(gitRepoRequest);
        } else if (gitRepo.get().getGitRepoMembers().isEmpty()) {
            return requestToGithub(gitRepoRequest);
        }
        return gitRepoMemberService.findAllByGitRepo(gitRepo.get()).stream()
                .map(gitRepoMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "twoRepoMemberResponseList", key = "#request", cacheManager = "cacheManager", unless = "#result.firstResult.size() == 0 || #result.secondResult.size() == 0")
    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(GitRepoCompareRequest request) {
        Integer year = LocalDate.now().getYear();
        String githubToken = authService.getLoginUser().getGithubToken();
        String firstRepo = request.getFirstRepo();
        String secondRepo = request.getSecondRepo();

        List<GitRepoMemberResponse> firstResult = findMembersByGitRepoWithClient(new GitRepoRequest(githubToken, firstRepo, year));
        List<GitRepoMemberResponse> secondResult = findMembersByGitRepoWithClient(new GitRepoRequest(githubToken, secondRepo, year));

        requestIssueToScraping(new GitRepoNameRequest(firstRepo));
        requestIssueToScraping(new GitRepoNameRequest(secondRepo));

        return new TwoGitRepoMemberResponse(firstResult, secondResult);
    }

    @Cacheable(value = "gitRepoMemberCompareResponse", key = "#request", cacheManager = "cacheManager", unless = "#result.firstMember == null || #result.secondMember == null")
    public GitRepoMemberCompareResponse findTwoGitRepoMember(GitRepoMemberCompareRequest request) {
        GitRepoMember first = gitRepoMemberService.findByNameAndMemberName(request.getFirstRepo(), request.getFirstName());
        GitRepoMember second = gitRepoMemberService.findByNameAndMemberName(request.getSecondRepo(), request.getSecondName());

        return new GitRepoMemberCompareResponse(gitRepoMemberMapper.toResponse(first), gitRepoMemberMapper.toResponse(second));
    }

    @Cacheable(value = "twoGitRepoResponse", key = "#request", cacheManager = "cacheManager", unless = "#result.firstRepo.gitRepo.closed_issues_count == null || #result.secondRepo.gitRepo.closed_issues_count == null")
    public TwoGitRepoResponse findTwoGitRepos(GitRepoCompareRequest request) {
        return new TwoGitRepoResponse(getOneRepoResponse(request.getFirstRepo()), getOneRepoResponse(request.getSecondRepo()));
    }

    @Transactional
    public void updateClosedIssues(String name, Integer closedIssue) {
        GitRepo gitRepo = gitRepoRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
        gitRepo.updateClosedIssues(closedIssue);
    }

    private GitRepoResponse getOneRepoResponse(String repoName) {
        Integer year = LocalDate.now().getYear();
        String githubToken = authService.getLoginUser().getGithubToken();
        GitRepo repo = gitRepoRepository.findByName(repoName).orElseGet(() -> gitRepoRepository.save(gitRepoMapper.toEntity(new GitRepoRequest(repoName, year))));
        GitRepoClientResponse repoResponse = gitRepoClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));
        if (repo.getClosedIssues() != null) {
            repoResponse.setClosed_issues_count(repo.getClosedIssues());
        } else {
            requestIssueToScraping(new GitRepoNameRequest(repoName));
        }
        Map<String, Integer> languages = gitRepoLanguageClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));
        IntSummaryStatistics langStats =
                languages.keySet().isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0)
                        : languages.keySet().stream().mapToInt(languages::get).summaryStatistics();

        return new GitRepoResponse(repoResponse, getStatistics(repoName), languages, langStats);
    }

    private StatisticsResponse getStatistics(String name) {
        GitRepo gitRepo = getEntityByName(name);
        List<Integer> commits = gitRepo.getGitRepoMembers().stream().map(g -> g.getContribution().getCommits()).collect(Collectors.toList());
        List<Integer> additions = gitRepo.getGitRepoMembers().stream().map(g -> g.getContribution().getAdditions()).collect(Collectors.toList());
        List<Integer> deletions = gitRepo.getGitRepoMembers().stream().map(g -> g.getContribution().getDeletions()).collect(Collectors.toList());

        return new StatisticsResponse(
                commits.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : commits.stream().mapToInt(Integer::intValue).summaryStatistics(),
                additions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : additions.stream().mapToInt(Integer::intValue).summaryStatistics(),
                deletions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : deletions.stream().mapToInt(Integer::intValue).summaryStatistics());
    }

    private GitRepo getEntityByName(String name) {
        return gitRepoRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    private void requestGitRepoToScraping(GitRepoRequest gitRepoRequest) {
        kafkaGitRepoProducer.send(gitRepoRequest);
    }

    private void requestIssueToScraping(GitRepoNameRequest gitRepoNameRequest) {
        kafkaIssueProducer.send(gitRepoNameRequest);
    }

    private List<GitRepoMemberResponse> requestToGithub(GitRepoRequest gitRepoRequest) {
        GitRepoMemberClientResponse[] clientResponse = gitRepoMemberClient.requestToGithub(gitRepoRequest);
        if (clientResponse == null || clientResponse.length == 0) {
            return List.of();
        }

        List<GitRepoMemberClientResponse> gitRepoClientResponse = Arrays.asList(clientResponse);

        Map<GitRepoMemberClientResponse, Integer> additions = gitRepoClientResponse.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> Arrays.asList(mem.getWeeks()).stream().mapToInt(w -> w.getA()).sum()));

        Map<GitRepoMemberClientResponse, Integer> deletions = gitRepoClientResponse.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> Arrays.asList(mem.getWeeks()).stream().mapToInt(w -> w.getD()).sum()));

        List<GitRepoMemberResponse> result = gitRepoClientResponse.stream()
                .map(member -> new GitRepoMemberResponse(
                        member.getAuthor().getLogin(),
                        member.getTotal(),
                        additions.get(member),
                        deletions.get(member))).collect(Collectors.toList());

        gitRepoMemberService.saveAll(result, gitRepoRequest.getName());

        return result;
    }
}
