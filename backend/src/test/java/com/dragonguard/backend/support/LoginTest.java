package com.dragonguard.backend.support;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.member.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@DatabaseTest
public abstract class LoginTest {

    @MockBean
    protected AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    protected Member loginUser;

    @BeforeEach
    private void setup() {
        Member member = new Member("Kim", "ohksj77", new Commit(2023, 100, "ohksj77"), "12341234", "https://github", "ohksj77@gmail.com");
        loginUser = memberRepository.save(member);
        when(authService.getLoginUser()).thenReturn(loginUser);
    }
}
