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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GitRepoIssueClient implements GithubClient<GitRepoClientRequest, Integer> {
    private final WebClient webClient;

    @Override
    public Integer requestToGithub(GitRepoClientRequest request) {
        int page = 1;
        List<String> results = new ArrayList<>(getClosedIssueNum(request, page++));
        if (results.isEmpty()) return 0;

        while (true) {
            List<String> closedIssues = getClosedIssueNum(request, page++);
            results.addAll(closedIssues);
            if (closedIssues.isEmpty()) break;
        }
        return results.stream().distinct().mapToInt(r -> 1).sum();
    }

    private List<String> getClosedIssueNum(GitRepoClientRequest request, int page) {
        return Arrays.stream(webClient.get()
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
                .orElseThrow(WebClientException::new))
                .filter(response -> Objects.nonNull(response.getPullRequest()))
                .map(GitRepoIssueResponse::getUrl)
                .collect(Collectors.toList());
    }
}
