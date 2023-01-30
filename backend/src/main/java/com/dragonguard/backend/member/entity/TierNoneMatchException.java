package com.dragonguard.backend.member.entity;

import com.dragonguard.backend.global.exception.BusinessException;

public class TierNoneMatchException extends BusinessException {

    private static final String MESSAGE = "티어 계산에 오류가 발생했습니다.";

    public TierNoneMatchException() {
        super(MESSAGE);
    }
}
