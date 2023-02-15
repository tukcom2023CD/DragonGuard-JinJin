package com.dragonguard.backend.search.client;

import com.dragonguard.backend.global.exception.WebClientException;
import com.dragonguard.backend.global.webclient.GithubClient;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.dto.response.SearchClientResponse;
import com.dragonguard.backend.search.entity.SearchType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class SearchClient implements GithubClient<SearchRequest, SearchClientResponse> {
    private static final String BASE_URL = "${scrapping.url}";
    private final WebClient webClient;

    public SearchClient(@Value(BASE_URL) String baseUrl) {
        webClient = generateWebClient(baseUrl);
    }

    @Override
    public SearchClientResponse requestToGithub(SearchRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("search")
                                .path("/" + request.getType().toString().toLowerCase())
                                .queryParam("q", request.getName())
                                .queryParam("per_page", 10)
                                .queryParam("page", request.getPage())
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(SearchClientResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }

    private WebClient generateWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
