package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsMapper;
import com.dragonguard.backend.domain.member.dto.request.kafka.KafkaContributionRequest;
import com.dragonguard.backend.domain.member.dto.request.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.messagequeue.KafkaRepositoryClientProducer;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.util.KafkaProducer;
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

        Member user = memberRepository.findMemberByGithubId(githubId)
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(githubId, Role.ROLE_USER, AuthStep.GITHUB_ONLY)));

        kafkaContributionClientProducer.send(new KafkaContributionRequest(githubId));
        kafkaRepositoryClientProducer.send(new KafkaRepositoryRequest(githubId));

        user.updateGithubToken(userRequest.getAccessToken().getTokenValue());

        return userDetailsMapper.mapToLoginUser(user);
    }
}
