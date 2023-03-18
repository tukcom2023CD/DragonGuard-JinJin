package com.dragonguard.backend.gitrepo.controller;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.dto.response.GirRepoMemberCompareResponse;
import com.dragonguard.backend.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.gitrepo.service.GitRepoService;
import com.dragonguard.backend.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 요청을 처리하는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/git-repos")
public class GitRepoController {
    private final GitRepoService gitRepoService;

    @GetMapping
    public ResponseEntity<List<GitRepoMemberResponse>> getRepoMembers(
            @RequestHeader String githubToken,
            @RequestParam String name) {
        return ResponseEntity.ok(gitRepoService.findMembersByGitRepoWithClient(new GitRepoRequest(githubToken, name, LocalDate.now().getYear())));
    }

    @PostMapping("/compare")
    public ResponseEntity<TwoGitRepoResponse> getTwoGitRepos(
            @RequestHeader String githubToken,
            @RequestBody GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findTwoGitRepos(githubToken, request));
    }

    @PostMapping("/compare/git-repos-members")
    public ResponseEntity<TwoGitRepoMemberResponse> getGitRepoMembersForCompare(
            @RequestHeader String githubToken,
            @RequestBody GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findMembersByGitRepoForCompare(githubToken, request));
    }

    @PostMapping("/compare/members")
    public ResponseEntity<GirRepoMemberCompareResponse> getTwoGitRepoMember(@RequestBody GitRepoMemberCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findTwoGitRepoMember(request));
    }
}
