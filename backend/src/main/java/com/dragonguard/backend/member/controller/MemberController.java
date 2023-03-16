package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<IdResponse<UUID>> saveMember(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(memberService.saveMember(memberRequest));
    }

    @PostMapping("/commits")
    public ResponseEntity<Void> updateCommits() {
        memberService.updateCommits();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tier")
    public ResponseEntity<Tier> getTier() {
        return ResponseEntity.ok(memberService.getTier());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<MemberRankResponse>> getTier(
            @PageableDefault(sort = "tokens", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMemberRanking(pageable));
    }

    @PostMapping("/wallet-address")
    public ResponseEntity<Void> updateWalletAddress(@RequestBody WalletRequest walletRequest) {
        memberService.updateWalletAddress(walletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getCurrentUser() {
        return ResponseEntity.ok(memberService.getMember());
    }
}
