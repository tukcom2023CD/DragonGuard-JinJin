package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DatabaseTest
public class MemberServiceTest extends LoginTest {
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("")
    public void findMemberDetail() {
        
    }
}
