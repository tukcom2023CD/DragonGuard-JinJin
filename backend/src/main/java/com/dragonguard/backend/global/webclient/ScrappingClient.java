package com.dragonguard.backend.global.webclient;

public interface ScrappingClient<T> {
    void requestToScrapping(T request);
}
