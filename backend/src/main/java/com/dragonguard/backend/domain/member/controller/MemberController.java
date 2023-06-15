package com.dragonguard.backend.domain.member.controller;

import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.MemberGitReposAndGitOrganizationsResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberResponse;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.entity.Tier;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 관련 요청을 처리하는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<IdResponse<UUID>> saveMember(@RequestBody @Valid MemberRequest memberRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.saveMember(memberRequest, Role.ROLE_USER));
    }

    @PostMapping("/contributions")
    public ResponseEntity<Void> updateContribution() {
        memberService.updateContributions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/blockchains")
    public ResponseEntity<Void> updateBlockchain() {
        memberService.updateBlockchain();
        return ResponseEntity.noContent().build();
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
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getCurrentUser() {
        return ResponseEntity.ok(memberService.getMember());
    }

    @GetMapping
    public ResponseEntity<MemberGitReposAndGitOrganizationsResponse> getMemberDetail(@RequestParam String githubId) {
        return ResponseEntity.ok(memberService.findMemberDetailByGithubId(githubId));
    }
}
