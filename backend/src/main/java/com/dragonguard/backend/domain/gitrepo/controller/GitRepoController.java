package com.dragonguard.backend.domain.gitrepo.controller;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoMemberFacade;

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
    private final GitRepoMemberFacade gitRepoMemberFacade;

    /** 깃허브 레포지토리의 기여자들의 정보 조회 api */
    @GetMapping
    public ResponseEntity<GitRepoResponse> getGitRepoMembers(@RequestParam final String name) {
        return ResponseEntity.ok(gitRepoMemberFacade.findGitRepoInfos(name));
    }

    /** 깃허브 레포지토리의 정보 비교를 위한 조회 api */
    @PostMapping("/compare")
    public ResponseEntity<TwoGitRepoResponse> getTwoGitRepos(
            @RequestBody @Valid final GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoMemberFacade.findTwoGitRepos(request));
    }

    /** 깃허브 레포지토리의 기여자들의 정보 비교를 위한 조회 api */
    @PostMapping("/compare/git-repos-members")
    public ResponseEntity<TwoGitRepoMemberResponse> getGitRepoMembersForCompare(
            @RequestBody @Valid final GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoMemberFacade.findMembersByGitRepoForCompare(request));
    }

    /** 깃허브 레포지토리의 기여자들의 정보 조회 (수동 업데이트) api */
    @GetMapping("/update")
    public ResponseEntity<GitRepoResponse> getGitRepoMembersAndUpdate(
            @RequestParam final String name) {
        return ResponseEntity.ok(gitRepoMemberFacade.findGitRepoInfosAndUpdate(name));
    }

    /** 깃허브 레포지토리의 정보 비교를 위한 조회 (수동 업데이트) api */
    @PostMapping("/compare/update")
    public ResponseEntity<TwoGitRepoResponse> getTwoGitReposAndUpdate(
            @RequestBody @Valid final GitRepoCompareRequest request) {
        return ResponseEntity.ok(gitRepoMemberFacade.findTwoGitReposAndUpdate(request));
    }

    /** 깃허브 레포지토리의 기여자들의 정보 비교를 위한 조회 (수동 업데이트) api */
    @PostMapping("/compare/git-repos-members/update")
    public ResponseEntity<TwoGitRepoMemberResponse> getGitRepoMembersForCompareAndUpdate(
            @RequestBody @Valid final GitRepoCompareRequest request) {
        return ResponseEntity.ok(
                gitRepoMemberFacade.findMembersByGitRepoForCompareAndUpdate(request));
    }

    /** 깃허브 레포지토리들의 두 기여자의 기여도 정보 비교를 위한 조회 api */
    @PostMapping("/compare/members")
    public ResponseEntity<GitRepoMemberCompareResponse> getTwoGitRepoMember(
            @RequestBody @Valid final GitRepoMemberCompareRequest request) {
        return ResponseEntity.ok(gitRepoMemberFacade.findTwoGitRepoMember(request));
    }
}
