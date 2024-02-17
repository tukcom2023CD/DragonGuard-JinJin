package com.dragonguard.backend.config.cronjob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author 김승진
 * @description cronjob 관련 application.yml의 환경변수를 받아오는 클래스
 */
@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cronjob")
public class CronjobProperties {
    private final String header;
    private final String auth;
}
