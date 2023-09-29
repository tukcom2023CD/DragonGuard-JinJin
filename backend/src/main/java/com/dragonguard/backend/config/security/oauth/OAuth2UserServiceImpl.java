package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsMapper;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaContributionRequest;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import com.dragonguard.backend.global.template.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

/**
 * @author 김승진
 * @description OAuth2UserService의 구현체로 유저를 로드한다.
 */

@TransactionService
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final UserDetailsMapper userDetailsMapper;
    private final MemberMapper memberMapper;
    private final KafkaProducer<KafkaContributionRequest> kafkaContributionClientProducer;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);
        final Map<String, Object> attributes = oAuth2User.getAttributes();
        final String githubId = (String) attributes.get("login");
        final String profileImage = (String) attributes.get("avatar_url");
        final String name = (String) attributes.get("name");

        if (!memberRepository.existsByGithubId(githubId)) {
            memberRepository.save(memberMapper.toEntity(githubId, Role.ROLE_USER, AuthStep.GITHUB_ONLY, name, profileImage));
        }

        final Member user = memberRepository.findByGithubId(githubId)
                .orElseThrow(EntityNotFoundException::new);

        if (user.getAuthStep().isNone()) {
            user.updateAuthStepAndNameAndProfileImage(AuthStep.GITHUB_ONLY, name, profileImage);
        }

        final String githubToken = userRequest.getAccessToken().getTokenValue();
        user.updateGithubToken(githubToken);

        kafkaContributionClientProducer.send(new KafkaContributionRequest(githubId));
        return userDetailsMapper.mapToLoginUser(user);
    }
}
