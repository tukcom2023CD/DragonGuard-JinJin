package com.dragonguard.backend.config.security.oauth.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OAuth2UserInfo {
    private Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getLogin() {
        return (String) attributes.get("login");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getAvatarUrl() {
        return (String) attributes.get("avatar_url");
    }

    public String getName() {
        return (String) attributes.get("name");
    }
}
