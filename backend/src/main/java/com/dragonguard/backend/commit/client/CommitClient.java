package com.dragonguard.backend.commit.client;

import com.dragonguard.backend.commit.dto.request.CommitScrapingRequest;
import com.dragonguard.backend.global.exception.ScrapingRequestFailException;
import com.dragonguard.backend.global.webclient.ScrapingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class CommitClient implements ScrapingClient<CommitScrapingRequest> {

    private static final String BASE_URL = "${scraping.url}";
    private final WebClient webClient;

    public CommitClient(@Value(BASE_URL) String baseUrl) {
        webClient = generateWebClient(baseUrl);
    }

    @Override
    public void requestToScraping(CommitScrapingRequest request) {
        webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("commits")
                                .queryParam("member", request.getGithubId())
                                .queryParam("year", request.getYear())
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElseThrow(ScrapingRequestFailException::new);
    }

    private WebClient generateWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
