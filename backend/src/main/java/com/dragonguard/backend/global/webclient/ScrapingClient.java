package com.dragonguard.backend.global.webclient;

public interface ScrapingClient<T> {
    void requestToScraping(T request);
}
