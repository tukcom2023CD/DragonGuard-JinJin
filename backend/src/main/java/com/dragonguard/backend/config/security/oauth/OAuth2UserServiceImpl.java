package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsMapper;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaContributionRequest;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberQueryRepository;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author 김승진
 * @description OAuth2UserService의 구현체로 유저를 로드한다.
 */

@TransactionService
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;
    private final UserDetailsMapper userDetailsMapper;
    private final MemberMapper memberMapper;
    private final KafkaProducer<KafkaContributionRequest> kafkaContributionClientProducer;
    private final KafkaProducer<KafkaRepositoryRequest> kafkaRepositoryClientProducer;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String githubId = (String) attributes.get("login");

        Member user = memberQueryRepository.findByGithubId(githubId)
                .orElse(memberRepository.save(memberMapper.toEntity(githubId, Role.ROLE_USER, AuthStep.GITHUB_ONLY)));
        String githubToken = userRequest.getAccessToken().getTokenValue();
        user.updateGithubToken(githubToken);

        if (StringUtils.hasText(user.getWalletAddress()) && !user.getAuthStep().equals(AuthStep.GITHUB_ONLY)) {
            kafkaContributionClientProducer.send(new KafkaContributionRequest(githubId, githubToken));
        }
        kafkaRepositoryClientProducer.send(new KafkaRepositoryRequest(githubId, githubToken));

        return userDetailsMapper.mapToLoginUser(user);
    }
}
