package com.dragonguard.backend.support.database;

import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.support.fixture.member.entity.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

@TestExecutionListeners(MockitoTestExecutionListener.class)
public abstract class LoginTest {

    @MockBean
    AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    protected Member loginUser;

    @BeforeEach
    public void setup() {
        Member member = MemberFixture.SAMPLE1.toMember();
        loginUser = memberRepository.save(member);
        Mockito.when(authService.getLoginUser()).thenReturn(loginUser);
    }
}
