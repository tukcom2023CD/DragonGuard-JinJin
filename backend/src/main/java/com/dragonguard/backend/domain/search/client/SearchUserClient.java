package com.dragonguard.backend.domain.search.client;

import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.global.template.client.GithubClient;
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
    private static final int PER_PAGE_SIZE = 10;
    private static final String PATH_FORMAT = "search/%s?q=%s&per_page=%d&page=%d";
    private final WebClient webClient;

    @Override
    public SearchUserResponse requestToGithub(final SearchRequest request) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format(PATH_FORMAT, request.getType().getLowerCase(), request.getName(), PER_PAGE_SIZE, request.getPage()))
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
