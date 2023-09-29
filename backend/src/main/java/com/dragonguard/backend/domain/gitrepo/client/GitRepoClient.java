package com.dragonguard.backend.domain.gitrepo.client;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.global.template.client.GithubClient;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 정보를 Github REST API에 요청하는 클래스
 */

@Component
@RequiredArgsConstructor
public class GitRepoClient implements GithubClient<GitRepoClientRequest, GitRepoClientResponse> {
    private static final String PATH_FORMAT = "repos/%s";
    private final WebClient webClient;

    @Override
    public GitRepoClientResponse requestToGithub(final GitRepoClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(String.format(PATH_FORMAT, request.getName()))
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(GitRepoClientResponse.class)
                .blockOptional()
                .orElseThrow(WakeupException::new);
    }
}
