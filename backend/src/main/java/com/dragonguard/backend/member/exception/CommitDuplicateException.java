package com.dragonguard.backend.member.exception;

/**
 * @author 김승진
 * @description 멤버 커밋 중복 저장시 나타나는 예외
 */

public class CommitDuplicateException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 저장된 커밋 내역입니다.";

    public CommitDuplicateException() {
        super(MESSAGE);
    }
}
