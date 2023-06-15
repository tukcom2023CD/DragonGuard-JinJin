package com.dragonguard.backend.domain.gitrepo.client;

import com.dragonguard.backend.config.github.GithubProperties;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoSparkLineResponse;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class GitRepoSparkLineClient implements GithubClient<GitRepoClientRequest, GitRepoSparkLineResponse> {
    private final GithubProperties githubProperties;
    private final WebClient webClient;
    private static final String GITHUB_API_MIME_TYPE = "application/vnd.github+json";
    private static final String USER_AGENT = "GITRANK WEB CLIENT";

    public GitRepoSparkLineClient(GithubProperties githubProperties) {
        this.githubProperties = githubProperties;
        webClient = generateWebClient();
    }
    @Override
    public GitRepoSparkLineResponse requestToGithub(GitRepoClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("repos/")
                                .path(request.getName())
                                .path("/stats")
                                .path("/participation")
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(GitRepoSparkLineResponse.class)
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
