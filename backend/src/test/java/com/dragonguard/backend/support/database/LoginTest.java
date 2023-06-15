package com.dragonguard.backend.support.database;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.support.fixture.member.entity.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@DatabaseTest
public abstract class LoginTest {

    @MockBean
    protected AuthService authService;

    @Autowired
    protected MemberRepository memberRepository;

    protected Member loginUser;

    @BeforeEach
    public void setup() {
        Member member = MemberFixture.OHKSJ77.toEntity();
        loginUser = memberRepository.save(member);
        when(authService.getLoginUser()).thenReturn(loginUser);
        when(authService.getLoginUserId()).thenReturn(loginUser.getId());
    }
}
