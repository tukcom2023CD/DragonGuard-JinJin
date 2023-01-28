package com.dragonguard.backend.global.webclient;

public interface ScrappingClient<T, R> {
    void requestToScrapping(T request);
}
