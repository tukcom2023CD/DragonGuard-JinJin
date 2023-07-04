package com.dragonguard.backend.domain.member.controller;

import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.*;
import com.dragonguard.backend.domain.member.entity.Tier;
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

    @PostMapping("/contributions")
    public ResponseEntity<Void> updateContribution() {
        memberService.updateContributions();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/update")
    public ResponseEntity<MemberResponse> updateContributionsAndGetProfile() {
        return ResponseEntity.ok(memberService.updateContributionsAndGetProfile());
    }

    @PostMapping("/blockchains")
    public ResponseEntity<Void> updateBlockchain() {
        memberService.updateBlockchain();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tier")
    public ResponseEntity<Tier> getTier() {
        return ResponseEntity.ok(memberService.getTier());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<MemberRankResponse>> getRank(
            @PageableDefault(sort = "tokens", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMemberRanking(pageable));
    }

    @GetMapping("/ranking/organization")
    public ResponseEntity<List<MemberRankResponse>> getOrganizationMemberRank(
            @RequestParam Long organizationId,
            @PageableDefault(sort = "tokens", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMemberRankingByOrganization(organizationId, pageable));
    }

    @PostMapping("/wallet-address")
    public ResponseEntity<Void> updateWalletAddress(@RequestBody @Valid WalletRequest walletRequest) {
        memberService.updateWalletAddress(walletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getCurrentUser() {
        return ResponseEntity.ok(memberService.getMember());
    }

    @GetMapping("/me/details")
    public ResponseEntity<MemberGitReposAndGitOrganizationsResponse> getCurrentUserDetails() {
        return ResponseEntity.ok(memberService.findMemberDetails());
    }

    @GetMapping("/details")
    public ResponseEntity<MemberDetailsResponse> getUserDetails(@RequestParam String githubId) {
        return ResponseEntity.ok(memberService.getMemberDetails(githubId));
    }

    @GetMapping("/git-organizations/git-repos")
    public ResponseEntity<MemberGitOrganizationRepoResponse> getMemberGitOrganizationRepo(@RequestParam String name) {
        return ResponseEntity.ok(memberService.getMemberGitOrganizationRepo(name));
    }
}
