package com.dragonguard.backend.member.entity;

import com.dragonguard.backend.global.exception.GitRankException;

public class CommitDuplicateException extends GitRankException {

    private static final String MESSAGE = "이미 저장된 커밋 내역입니다.";

    public CommitDuplicateException() {
        super(MESSAGE);
    }
}
