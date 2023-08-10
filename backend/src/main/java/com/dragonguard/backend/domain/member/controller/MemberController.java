package com.dragonguard.backend.domain.member.controller;

import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.*;
import com.dragonguard.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 김승진
 * @description 멤버 관련 요청을 처리하는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    /**
     * 로그인한 유저의 기여도 정보 업데이트를 위한 api
     */
    @PostMapping("/contributions")
    public ResponseEntity<Void> updateContribution() {
        memberService.updateContributions();
        return ResponseEntity.ok().build();
    }

    /**
     * 로그인한 유저의 기본 정보 조회 위한 (수동 업데이트) api
     */
    @PostMapping("/me/update")
    public ResponseEntity<MemberResponse> updateContributionsAndGetProfile() {
        return ResponseEntity.ok(memberService.updateContributionsAndGetProfile());
    }

    /**
     * 로그인한 유저의 블록체인 정보 업데이트를 위한 api
     */
    @PostMapping("/blockchains")
    public ResponseEntity<Void> updateBlockchain() {
        memberService.updateBlockchain();
        return ResponseEntity.ok().build();
    }

    /**
     * 유저들의 개인 랭킹 정보를 조회하기 위한 api
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<MemberRankResponse>> getRank(
            @PageableDefault(sort = "tokens", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.findMemberRanking(pageable));
    }

    /**
     * 유저들의 조직에서의 개인 랭킹 정보를 조회하기 위한 api
     */
    @GetMapping("/ranking/organization")
    public ResponseEntity<List<MemberRankResponse>> getOrganizationMemberRank(
            @RequestParam Long organizationId,
            @PageableDefault(sort = "tokens", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.findMemberRankingByOrganization(organizationId, pageable));
    }

    /**
     * 유저들의 지갑 주소 업데이트를 위한 api
     */
    @PostMapping("/wallet-address")
    public ResponseEntity<Void> updateWalletAddress(@RequestBody @Valid WalletRequest walletRequest) {
        memberService.updateWalletAddress(walletRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 유저들의 기본 정보 조회를 위한 api
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getCurrentUser() {
        return ResponseEntity.ok(memberService.getMember());
    }

    /**
     * 로그인한 유저의 상세 정보 조회를 위한 api
     */
    @GetMapping("/me/details")
    public ResponseEntity<MemberGitReposAndGitOrganizationsResponse> getCurrentUserDetails() {
        return ResponseEntity.ok(memberService.findMemberDetails());
    }

    /**
     * 로그인한 유저를 제외한 다른 유저의 상세 정보 조회를 위한 api
     */
    @GetMapping("/details")
    public ResponseEntity<MemberDetailsResponse> getUserDetails(@RequestParam String githubId) {
        return ResponseEntity.ok(memberService.findMemberDetails(githubId));
    }

    /**
     * 로그인한 유저의 깃허브 organization 내부 repository 목록 조회를 위한 api
     */
    @GetMapping("/git-organizations/git-repos")
    public ResponseEntity<MemberGitOrganizationRepoResponse> getMemberGitOrganizationRepo(@RequestParam String name) {
        return ResponseEntity.ok(memberService.findMemberGitOrganizationRepo(name));
    }

    /**
     * 로그인한 유저가 아직 지갑 주소를 업데이트하지 않았는지 확인하는 api
     */
    @GetMapping("/verify")
    public ResponseEntity<MemberLoginVerifyResponse> getMemberLoginVerify() {
        return ResponseEntity.ok(memberService.verifyMember());
    }
}
