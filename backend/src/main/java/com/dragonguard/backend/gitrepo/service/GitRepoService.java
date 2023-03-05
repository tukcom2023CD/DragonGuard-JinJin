package com.dragonguard.backend.gitrepo.service;

import com.dragonguard.backend.gitrepo.client.GitRepoClient;
import com.dragonguard.backend.gitrepo.client.GitRepoLanguageClient;
import com.dragonguard.backend.gitrepo.client.GitRepoMemberClient;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.dto.response.*;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.gitrepo.messagequeue.KafkaGitRepoProducer;
import com.dragonguard.backend.gitrepo.messagequeue.KafkaIssueProducer;
import com.dragonguard.backend.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberClientResponse;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitRepoService {
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberService gitRepoMemberService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaGitRepoProducer kafkaGitRepoProducer;
    private final KafkaIssueProducer kafkaIssueProducer;
    private final GitRepoMemberClient gitRepoMemberClient;
    private final GitRepoClient gitRepoClient;
    private final GitRepoLanguageClient gitRepoLanguageClient;

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

    public List<GitRepoMemberResponse> findMembersByGitRepoWithClient(GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = gitRepoRepository.findByName(gitRepoRequest.getName());
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

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(GitRepoCompareRequest request) {
        Integer year = LocalDate.now().getYear();
        List<GitRepoMemberResponse> firstResult = findMembersByGitRepoWithClient(new GitRepoRequest(request.getFirstRepo(), year));
        List<GitRepoMemberResponse> secondResult = findMembersByGitRepoWithClient(new GitRepoRequest(request.getSecondRepo(), year));
        requestIssueToScraping(new GitRepoNameRequest(request.getFirstRepo()));
        requestIssueToScraping(new GitRepoNameRequest(request.getSecondRepo()));

        return new TwoGitRepoMemberResponse(firstResult, secondResult);
    }

    public GirRepoMemberCompareResponse findTwoGitRepoMember(GitRepoMemberCompareRequest request) {
        GitRepoMember first = gitRepoMemberService.findByNameAndMemberName(request.getFirstRepo(), request.getFirstName());
        GitRepoMember second = gitRepoMemberService.findByNameAndMemberName(request.getSecondRepo(), request.getSecondName());

        return new GirRepoMemberCompareResponse(gitRepoMemberMapper.toResponse(first), gitRepoMemberMapper.toResponse(second));
    }

    public TwoGitRepoResponse findTwoGitRepos(GitRepoCompareRequest request) {
        return new TwoGitRepoResponse(getOneRepoResponse(request.getFirstRepo()), getOneRepoResponse(request.getSecondRepo()));
    }

    private GitRepoResponse getOneRepoResponse(String repoName) {
        GitRepo repo = gitRepoRepository.findByName(repoName).orElseGet(() -> gitRepoRepository.save(gitRepoMapper.toEntity(new GitRepoRequest(repoName, null))));
        GitRepoClientResponse repoResponse = gitRepoClient.requestToGithub(repoName);
        if(repo.getClosedIssues() != null) repoResponse.setClosed_issues_count(repo.getClosedIssues());
        Map<String, Integer> languages = gitRepoLanguageClient.requestToGithub(repoName);
        IntSummaryStatistics langStats =
                languages.keySet().isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0)
                        : languages.keySet().stream().mapToInt(languages::get).summaryStatistics();

        return new GitRepoResponse(repoResponse, getStatistics(repoName), languages, langStats);
    }

    @Transactional
    public void updateClosedIssues(String name, Integer closedIssue) {
        GitRepo gitRepo = gitRepoRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
        gitRepo.updateClosedIssues(closedIssue);
    }

    private StatisticsResponse getStatistics(String name) {
        GitRepo gitRepo = getEntityByName(name);
        List<Integer> commits = gitRepo.getGitRepoMembers().stream().map(GitRepoMember::getCommits).collect(Collectors.toList());
        List<Integer> additions = gitRepo.getGitRepoMembers().stream().map(GitRepoMember::getAdditions).collect(Collectors.toList());
        List<Integer> deletions = gitRepo.getGitRepoMembers().stream().map(GitRepoMember::getDeletions).collect(Collectors.toList());

        return new StatisticsResponse(
                commits.isEmpty() ? new IntSummaryStatistics(0, 0, 0 ,0 ) : commits.stream().mapToInt(Integer::intValue).summaryStatistics(),
                additions.isEmpty() ? new IntSummaryStatistics(0, 0, 0 ,0 ) : additions.stream().mapToInt(Integer::intValue).summaryStatistics(),
                deletions.isEmpty() ? new IntSummaryStatistics(0, 0, 0 ,0 ) : deletions.stream().mapToInt(Integer::intValue).summaryStatistics());
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
                .collect(Collectors.toMap(Function.identity(), mem -> mem.getWeeks().stream().mapToInt(w -> w.getA()).sum()));

        Map<GitRepoMemberClientResponse, Integer> deletions = gitRepoClientResponse.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> mem.getWeeks().stream().mapToInt(w -> w.getD()).sum()));

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
