package com.dragonguard.backend.domain.search.client;

import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author 김승진
 * @description Repository 검색에 대한 Github REST API 요청을 수행하는 클래스
 */

@Component
@RequiredArgsConstructor
public class SearchRepoClient implements GithubClient<SearchRequest, SearchRepoResponse> {
    private static final int PER_PAGE_SIZE = 10;
    private static final String PATH_FORMAT = "search/%s?q=%s&per_page=%d&page=%d";
    private static final String FILTER_DELIMITER = "%%20";
    private final WebClient webClient;

    @Override
    public SearchRepoResponse requestToGithub(final SearchRequest request) {
        return webClient.get()
                .uri(getUriBuilder(request))
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(SearchRepoResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }

    private Function<UriBuilder, URI> getUriBuilder(final SearchRequest request) {
        List<String> filters = request.getFilters();

        if (Objects.isNull(filters) || filters.isEmpty()) {
            return uriBuilder -> uriBuilder
                    .path(String.format(String.format(
                            PATH_FORMAT,
                            request.getType().getLowerCase(),
                            request.getName(),
                            PER_PAGE_SIZE,
                            request.getPage())))
                    .build();
        }
        return uriBuilder -> uriBuilder
                .path(String.format(String.format(
                        PATH_FORMAT,
                        request.getType().getLowerCase(),
                        request.getName().strip().concat(FILTER_DELIMITER + String.join(FILTER_DELIMITER, filters)),
                        PER_PAGE_SIZE,
                        request.getPage())))
                .build();
    }
}
