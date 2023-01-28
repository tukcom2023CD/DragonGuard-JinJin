package com.dragonguard.backend.global.exception;

public abstract class GitRankException extends IllegalStateException {
    public GitRankException(String message) {
        super(message);
    }
}
