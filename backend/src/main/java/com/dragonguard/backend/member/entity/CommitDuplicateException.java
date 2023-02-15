package com.dragonguard.backend.member.entity;


public class CommitDuplicateException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 저장된 커밋 내역입니다.";

    public CommitDuplicateException() {
        super(MESSAGE);
    }
}
