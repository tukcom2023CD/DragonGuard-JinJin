package com.dragonguard.backend.global.template.client;

/**
 * @author 김승진
 * @description Github REST API 통신에서 쓰일 WebClient의 공통 기능을 뽑아낸 제네릭 인터페이스
 */
public interface GithubClient<T, R> {
    R requestToGithub(final T request);
}
