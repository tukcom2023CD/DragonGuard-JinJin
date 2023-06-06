package com.dragonguard.backend.config.github;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 관련 application.yml의 환경변수를 받아오는 클래스
 */

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "github")
public class GithubProperties {
    private String url;
    private String versionKey;
    private String versionValue;
}
