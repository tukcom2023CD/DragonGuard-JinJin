package com.dragonguard.backend.gitrepo.client;

import com.dragonguard.backend.config.github.GithubProperties;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberClientResponse;
import com.dragonguard.backend.global.exception.WebClientException;
import com.dragonguard.backend.util.GithubClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

/**
 * @author 김승진
 * @description 깃허브 Repository 멤버의 기여도 정보를 Github REST API에 요청하는 클래스
 */

@Component
public class GitRepoMemberClient implements GithubClient<GitRepoRequest, GitRepoMemberClientResponse[]> {
    private final GithubProperties githubProperties;
    private final WebClient webClient;
    private static final String GITHUB_API_MIME_TYPE = "application/vnd.github+json";
    private static final String USER_AGENT = "SPRING BOOT WEB CLIENT";

    public GitRepoMemberClient(GithubProperties githubProperties) {
        this.githubProperties = githubProperties;
        webClient = generateWebClient();
    }

    @Override
    public GitRepoMemberClientResponse[] requestToGithub(GitRepoRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("repos/")
                                .path(request.getName())
                                .path("/stats")
                                .path("/contributors")
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(GitRepoMemberClientResponse[].class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
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
