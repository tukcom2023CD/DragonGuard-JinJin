package com.dragonguard.backend.domain.search.entity;

import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 검색 유형을 구분하는 enum
 */
@RequiredArgsConstructor
public enum SearchType {
    USERS("유저"),
    REPOSITORIES("레포지토리");

    private final String korean;

    public String getLowerCase() {
        return toString().toLowerCase();
    }
}
