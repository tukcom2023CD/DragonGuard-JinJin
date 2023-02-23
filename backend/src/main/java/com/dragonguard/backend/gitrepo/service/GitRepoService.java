package com.dragonguard.backend.gitrepo.service;

import com.dragonguard.backend.gitrepo.client.GitRepoClient;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.gitrepo.messagequeue.KafkaGitRepoProducer;
import com.dragonguard.backend.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberClientResponse;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.gitrepomember.service.GitRepoMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitRepoService {
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final GitRepoMemberService gitRepoMemberService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaGitRepoProducer kafkaGitRepoProducer;
    private final GitRepoClient gitRepoClient;

    public List<GitRepoMemberResponse> findMembersByGitRepo(GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = gitRepoRepository.findByName(gitRepoRequest.getName());
        if (gitRepo.isEmpty()) {
            gitRepoRepository.save(gitRepoMapper.toEntity(gitRepoRequest));
            requestToScraping(gitRepoRequest);
            return List.of();
        } else if (gitRepo.get().getGitRepoMember().isEmpty()) {
            requestToScraping(gitRepoRequest);
            return List.of();
        }
        return gitRepoMemberRepository.findAllByGitRepo(gitRepo.get()).stream()
                .map(gitRepoMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<GitRepoMemberResponse> findMembersByGitRepoWithClient(GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = gitRepoRepository.findByName(gitRepoRequest.getName());
        if (gitRepo.isEmpty()) {
            GitRepo findGitRepo = gitRepoRepository.save(gitRepoMapper.toEntity(gitRepoRequest));
            return requestToGithub(gitRepoRequest);
        } else if (gitRepo.get().getGitRepoMember().isEmpty()) {
            return requestToGithub(gitRepoRequest);
        }
        return gitRepoMemberRepository.findAllByGitRepo(gitRepo.get()).stream()
                .map(gitRepoMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(GitRepoCompareRequest request) {
        Integer year = LocalDate.now().getYear();
        List<GitRepoMemberResponse> firstResult = findMembersByGitRepoWithClient(new GitRepoRequest(request.getFirstRepo(), year));
        List<GitRepoMemberResponse> secondResult = findMembersByGitRepoWithClient(new GitRepoRequest(request.getSecondRepo(), year));

        return new TwoGitRepoMemberResponse(firstResult, secondResult);
    }

    private void requestToScraping(GitRepoRequest gitRepoRequest) {
        kafkaGitRepoProducer.send(gitRepoRequest);
    }

    private List<GitRepoMemberResponse> requestToGithub(GitRepoRequest gitRepoRequest) {
        GitRepoMemberClientResponse[] clientResponse = gitRepoClient.requestToGithub(gitRepoRequest);
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

    public List<GitRepoMemberResponse> findTwoGitRepoMember(GitRepoMemberCompareRequest request) {
        GitRepoMember first = gitRepoMemberRepository.findByNameAndMemberName(request.getFirstRepo(), request.getFirstName());
        GitRepoMember second = gitRepoMemberRepository.findByNameAndMemberName(request.getSecondRepo(), request.getSecondName());

        return List.of(gitRepoMemberMapper.toResponse(first), gitRepoMemberMapper.toResponse(second));
    }
}
