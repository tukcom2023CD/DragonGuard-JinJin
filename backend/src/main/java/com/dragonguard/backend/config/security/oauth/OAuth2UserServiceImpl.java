package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsMapper;
import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author 김승진
 * @description OAuth2UserService의 구현체로 유저를 로드한다.
 */

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Member user = memberService.findMemberByGithubId((String) attributes.get("login"), AuthStep.GITHUB_ONLY);

        user.updateGithubToken(userRequest.getAccessToken().getTokenValue());

        return userDetailsMapper.mapToLoginUser(user);
    }
}
