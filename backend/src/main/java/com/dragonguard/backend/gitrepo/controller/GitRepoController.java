package com.dragonguard.backend.gitrepo.controller;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.service.GitRepoService;
import com.dragonguard.backend.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/git-repos")
public class GitRepoController {

    private final GitRepoService gitRepoService;

    @GetMapping
    public ResponseEntity<List<GitRepoMemberResponse>> getRepoMembers(@RequestParam String name) {
        return ResponseEntity.ok(gitRepoService.findMembersByGitRepoWithClient(new GitRepoRequest(name, LocalDate.now().getYear())));
    }

    @PostMapping("/compare")
    public ResponseEntity<TwoGitRepoMemberResponse> getRepoMembersForCompare(@RequestBody GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findMembersByGitRepoForCompare(request));
    }

    @PostMapping("/compare/members")
    public ResponseEntity<List<GitRepoMemberResponse>> getTwoRepoMember(@RequestBody GitRepoMemberCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findTwoGitRepoMember(request));
    }
}
