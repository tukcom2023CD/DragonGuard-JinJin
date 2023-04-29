package com.dragonguard.backend.domain.member.exception;

/**
 * @author 김승진
 * @description 멤버 티어가 잘못되었거나 계산할 수 없을 때 나타나는 예외
 */

public class TierNoneMatchException extends IllegalArgumentException {

    private static final String MESSAGE = "티어 계산에 오류가 발생했습니다.";

    public TierNoneMatchException() {
        super(MESSAGE);
    }
}
