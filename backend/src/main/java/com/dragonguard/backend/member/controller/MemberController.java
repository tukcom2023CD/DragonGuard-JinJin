package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> saveMember(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(memberService.saveMember(memberRequest));
    }

    @PostMapping("/{id}/commits")
    public ResponseEntity<Void> updateCommits(@PathVariable Long id) {
        memberService.updateCommits(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/tier")
    public ResponseEntity<Tier> getTier(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getTier(id));
    }
}
