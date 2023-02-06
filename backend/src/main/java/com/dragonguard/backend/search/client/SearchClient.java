package com.dragonguard.backend.search.client;

import com.dragonguard.backend.global.exception.ScrapingRequestFailException;
import com.dragonguard.backend.global.webclient.ScrapingClient;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class SearchClient implements ScrapingClient<SearchRequest> {

    private static final String BASE_URL = "${scraping.url}";
    private final WebClient webClient;

    public SearchClient(@Value(BASE_URL) String baseUrl) {
        webClient = generateWebClient(baseUrl);
    }

    private WebClient generateWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public void requestToScraping(SearchRequest request) {
        webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("search")
                                .queryParam("name", request.getName())
                                .queryParam("type", request.getType().toString().toLowerCase())
                                .queryParam("page", request.getPage())
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElseThrow(ScrapingRequestFailException::new);
    }
}
