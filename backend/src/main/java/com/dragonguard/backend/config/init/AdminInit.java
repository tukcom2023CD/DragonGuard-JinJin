package com.dragonguard.backend.config.init;

import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@DependsOnDatabaseInitialization
public class AdminInit {
    private final MemberService memberService;

    @PostConstruct
    public void initMember() {
        memberService.initMember();
    }
}
