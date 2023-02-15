package com.dragonguard.backend.gitrepo.client;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoClientResponse;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberClientResponse;
import com.dragonguard.backend.global.exception.WebClientException;
import com.dragonguard.backend.global.webclient.GithubClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GitRepoClient implements GithubClient<GitRepoRequest, GitRepoMemberClientResponse[]> {

    private static final String BASE_URL = "${scrapping.url}";
    private final WebClient webClient;

    public GitRepoClient(@Value(BASE_URL) String baseUrl) {
        webClient = generateWebClient(baseUrl);
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
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(GitRepoMemberClientResponse[].class)
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
