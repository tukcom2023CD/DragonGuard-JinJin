package com.dragonguard.backend.member.entity;


public class TierNoneMatchException extends IllegalArgumentException {

    private static final String MESSAGE = "티어 계산에 오류가 발생했습니다.";

    public TierNoneMatchException() {
        super(MESSAGE);
    }
}
