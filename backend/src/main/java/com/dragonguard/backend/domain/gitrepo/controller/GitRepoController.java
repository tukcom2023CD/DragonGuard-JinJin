package com.dragonguard.backend.domain.gitrepo.controller;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.domain.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<GitRepoResponse> getGitRepoMembers(
            @RequestParam String name) {
        return ResponseEntity.ok(gitRepoService.findGitRepoInfos(name));
    }

    @PostMapping("/compare")
    public ResponseEntity<TwoGitRepoResponse> getTwoGitRepos(
            @RequestBody @Valid GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findTwoGitRepos(request));
    }

    @PostMapping("/compare/git-repos-members")
    public ResponseEntity<TwoGitRepoMemberResponse> getGitRepoMembersForCompare(
            @RequestBody @Valid GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findMembersByGitRepoForCompare(request));
    }

    @GetMapping("/update")
    public ResponseEntity<GitRepoResponse> getGitRepoMembersAndUpdate(
            @RequestParam String name) {
        return ResponseEntity.ok(gitRepoService.findGitRepoInfosAndUpdate(name));
    }

    @PostMapping("/compare/update")
    public ResponseEntity<TwoGitRepoResponse> getTwoGitReposAndUpdate(
            @RequestBody @Valid GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findTwoGitReposAndUpdate(request));
    }

    @PostMapping("/compare/git-repos-members/update")
    public ResponseEntity<TwoGitRepoMemberResponse> getGitRepoMembersForCompareAndUpdate(
            @RequestBody @Valid GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findMembersByGitRepoForCompareAndUpdate(request));
    }

    @PostMapping("/compare/members")
    public ResponseEntity<GitRepoMemberCompareResponse> getTwoGitRepoMember(
            @RequestBody @Valid GitRepoMemberCompareRequest request) {
        return ResponseEntity.ok(gitRepoService.findTwoGitRepoMember(request));
    }
}
