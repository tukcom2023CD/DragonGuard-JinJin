package com.dragonguard.backend.domain.search.client;

import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

/**
 * @author 김승진
 * @description Repository 검색에 대한 Github REST API 요청을 수행하는 클래스
 */

@Component
@RequiredArgsConstructor
public class SearchUserClient implements GithubClient<SearchRequest, SearchUserResponse> {
    private final WebClient webClient;

    @Override
    public SearchUserResponse requestToGithub(SearchRequest request) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("search")
                        .path("/" + request.getType().toString().toLowerCase())
                        .queryParam("q", request.getName())
                        .queryParam("per_page", 10)
                        .queryParam("page", request.getPage())
                        .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(SearchUserResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
