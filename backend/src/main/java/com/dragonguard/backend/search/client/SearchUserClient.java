package com.dragonguard.backend.search.client;

import com.dragonguard.backend.config.github.GithubProperties;
import com.dragonguard.backend.global.exception.WebClientException;
import com.dragonguard.backend.util.GithubClient;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.dto.response.client.SearchUserResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description Repository 검색에 대한 Github REST API 요청을 수행하는 클래스
 */

@Component
public class SearchUserClient implements GithubClient<SearchRequest, SearchUserResponse> {
    private final GithubProperties githubProperties;
    private final WebClient webClient;
    private static final String GITHUB_API_MIME_TYPE = "application/vnd.github+json";
    private static final String USER_AGENT = "SPRING BOOT WEB CLIENT";

    public SearchUserClient(GithubProperties githubProperties) {
        this.githubProperties = githubProperties;
        this.webClient = generateWebClient();
    }

    @Override
    public SearchUserResponse requestToGithub(SearchRequest request) {
        return webClient.get()
                .uri(getUriBuilder(request))
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(SearchUserResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }

    private Function<UriBuilder, URI> getUriBuilder(SearchRequest request) {
        List<String> filters = request.getFilters();

        if (filters == null || filters.isEmpty()) {
            return uriBuilder -> uriBuilder
                    .path("search")
                    .path("/" + request.getType().toString().toLowerCase())
                    .queryParam("q", request.getName())
                    .queryParam("per_page", 10)
                    .queryParam("page", request.getPage())
                    .build();
        }
        String query = filters.stream().collect(Collectors.joining(" "));

        return uriBuilder -> uriBuilder
                .path("search")
                .path("/" + request.getType().toString().toLowerCase())
                .queryParam("q", request.getName().concat(" " + query))
                .queryParam("per_page", 10)
                .queryParam("page", request.getPage())
                .build();
    }

    private WebClient generateWebClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();
        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(githubProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_API_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(githubProperties.getVersionKey(), githubProperties.getVersionValue())
                .build();
    }
}
