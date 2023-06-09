package com.dragonguard.backend.config.github;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author 김승진
 * @description 깃허브 관련 application.yml의 환경변수를 받아오는 클래스
 */

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("github")
public class GithubProperties {
    private final String url;
    private final String versionKey;
    private final String versionValue;
}
