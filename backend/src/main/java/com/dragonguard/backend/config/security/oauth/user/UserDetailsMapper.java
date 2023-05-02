package com.dragonguard.backend.config.security.oauth.user;

import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDetailsMapper {
    public UserDetailsImpl mapToLoginUser(Member user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", user.getId());
        return new UserDetailsImpl(user, user.getRole(), attributes);
    }

    public UserDetailsImpl mapToLoginUser(Member user, Map<String, Object> attributes) {
        return new UserDetailsImpl(user, user.getRole(), attributes);
    }
}
