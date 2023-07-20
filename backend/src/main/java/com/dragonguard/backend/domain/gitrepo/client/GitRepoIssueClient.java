package com.dragonguard.backend.domain.gitrepo.client;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoIssueResponse;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GitRepoIssueClient implements GithubClient<GitRepoClientRequest, Integer> {
    private final WebClient webClient;

    @Override
    public Integer requestToGithub(GitRepoClientRequest request) {
        int page = 1;
        int result = 0;
        result += getClosedIssueNum(request, page++);
        if (result == 100) {
            return result + getClosedIssueNum(request, page);
        }
        return result;
    }

    private int getClosedIssueNum(GitRepoClientRequest request, int page) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("repos/")
                                .path(request.getName())
                                .path("/issues?state=closed&per_page=100&page=")
                                .path(Integer.toString(page))
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(GitRepoIssueResponse[].class)
                .blockOptional()
                .orElseThrow(WebClientException::new).length;
    }
}
