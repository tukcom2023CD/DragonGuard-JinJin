package com.dragonguard.backend.gitrepo.client;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.global.exception.ScrapingRequestFailException;
import com.dragonguard.backend.global.webclient.ScrapingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class GitRepoClient implements ScrapingClient<GitRepoRequest> {

    private static final String BASE_URL = "${scraping.url}";
    private final WebClient webClient;

    public GitRepoClient(@Value(BASE_URL) String baseUrl) {
        webClient = generateWebClient(baseUrl);
    }

    @Override
    public void requestToScraping(GitRepoRequest request) {
        webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("git-repos")
                                .queryParam("name", request.getName())
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
