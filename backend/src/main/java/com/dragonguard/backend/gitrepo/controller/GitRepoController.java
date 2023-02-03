package com.dragonguard.backend.gitrepo.controller;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.service.GitRepoService;
import com.dragonguard.backend.gitrepomember.dto.GitRepoMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/git-repos")
public class GitRepoController {

    private final GitRepoService gitRepoService;

    @GetMapping
    public ResponseEntity<List<GitRepoMemberResponse>> getRepoMembers(GitRepoRequest gitRepoRequest) {
        return ResponseEntity.ok(gitRepoService.findMembersByGitRepo(gitRepoRequest));
    }
}
