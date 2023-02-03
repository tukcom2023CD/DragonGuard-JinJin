package com.dragonguard.backend.gitrepo.service;

import com.dragonguard.backend.gitrepo.client.GitRepoClient;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.gitrepomember.dto.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitRepoService {
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberRepository gItRepoMemberRepository;
    private final GitRepoClient gitRepoClient;

    public List<GitRepoMemberResponse> findMembersByGitRepo(GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = gitRepoRepository.findByName(gitRepoRequest.getName());
        if (gitRepo.isPresent()) {
            return gItRepoMemberRepository.findAllByGitRepo(gitRepo.get()).stream()
                    .map(gitRepoMemberMapper::toResponse)
                    .collect(Collectors.toList());
        }
        requestToScrapping(gitRepoRequest);
        return List.of();
    }

    public GitRepo findGitRepoByName(String name) {
        return gitRepoRepository.findByName(name)
                .orElseGet(null);
    }

    private void requestToScrapping(GitRepoRequest gitRepoRequest) {
        gitRepoClient.requestToScrapping(gitRepoRequest);
    }
}
