package com.dragonguard.backend.domain.blockchain.entity;

import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 기여 종류를 구별하는 enum
 */
@RequiredArgsConstructor
public enum ContributeType {
    COMMIT("커밋"),
    ISSUE("이슈"),
    PULL_REQUEST("풀리퀘스트"),
    CODE_REVIEW("코드리뷰");

    private final String korean;
}
