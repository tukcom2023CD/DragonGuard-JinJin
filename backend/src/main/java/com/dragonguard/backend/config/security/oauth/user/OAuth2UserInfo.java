package com.dragonguard.backend.config.security.oauth.user;

import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2UserInfo {
    private Long id;
    private String name;
    private String email;
    private String avatar_url;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.id = (Long) attributes.get("id");
        this.name = (String) attributes.get("name");
        this.email = (String) attributes.get("email");
        this.avatar_url = (String) attributes.get("avatar_url");
    }
}
