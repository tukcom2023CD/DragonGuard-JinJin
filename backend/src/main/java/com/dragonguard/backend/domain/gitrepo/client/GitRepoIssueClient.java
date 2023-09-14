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
    private static final String PATH_FORMAT = "repos/%s/issues?state=closed&per_page=100&page=%d";
    private static final int FIRST_PAGE = 1;
    private static final int NO_ISSUE_NUM = 0;
    private static final int SUM_UNIT = 1;
    private final WebClient webClient;

    @Override
    public Integer requestToGithub(GitRepoClientRequest request) {
        int page = FIRST_PAGE;
        final List<String> results = new ArrayList<>(getClosedIssueNum(request, page++));
        if (results.isEmpty()) {
            return NO_ISSUE_NUM;
        }

        while (true) {
            final List<String> closedIssues = getClosedIssueNum(request, page++);
            results.addAll(closedIssues);
            if (closedIssues.isEmpty()) {
                break;
            }
        }
        return results.stream().distinct().mapToInt(r -> SUM_UNIT).sum();
    }

    private List<String> getClosedIssueNum(final GitRepoClientRequest request, final int page) {
        return Arrays.stream(webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(String.format(PATH_FORMAT, request.getName(), page))
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(GitRepoIssueResponse[].class)
                .blockOptional()
                .orElseThrow(WebClientException::new))
                .filter(response -> Objects.isNull(response.getPullRequest()))
                .map(GitRepoIssueResponse::getUrl)
                .collect(Collectors.toList());
    }
}
