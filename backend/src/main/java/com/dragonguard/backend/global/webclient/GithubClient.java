package com.dragonguard.backend.global.webclient;

public interface GithubClient<T, R> {
    R requestToGithub(T request);
}
