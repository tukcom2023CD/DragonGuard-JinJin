package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsMapper;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String githubId = (String) attributes.get("githubId");

        Member user = memberService.saveIfNone(new OAuth2Request(githubId));
        return userDetailsMapper.mapToLoginUser(user);
    }
}
