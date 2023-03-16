package com.dragonguard.backend.config.security.oauth.user;

import com.dragonguard.backend.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserDetailsMapper {
    public UserDetailsImpl mapToLoginUser(Member user, Map<String, Object> attributes) {
        return new UserDetailsImpl(user, user.getRole(), attributes);
    }
}
