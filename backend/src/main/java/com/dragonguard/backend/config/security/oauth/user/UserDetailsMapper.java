package com.dragonguard.backend.config.security.oauth.user;

import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 김승진
 * @description UserDetails를 Member로 Mapping해주는 클래스
 */

@Component
public class UserDetailsMapper {
    private static final String USER_ID = "id";

    public UserPrinciple mapToLoginUser(final Member user) {
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put(USER_ID, user.getId());
        return new UserPrinciple(user, user.getRole(), attributes);
    }
}
