package com.dragonguard.backend.domain.gitrepo.client;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepomember.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.ClientBadRequestException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author 김승진
 * @description 깃허브 Repository 멤버의 기여도 정보를 Github REST API에 요청하는 클래스
 */

@Component
@RequiredArgsConstructor
public class GitRepoMemberClient implements GithubClient<GitRepoInfoRequest, GitRepoMemberClientResponse[]> {
    private final WebClient webClient;

    @Override
    public GitRepoMemberClientResponse[] requestToGithub(GitRepoInfoRequest request) {
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
                .onStatus(hs -> hs.equals(HttpStatus.NO_CONTENT), response -> Mono.error(WebClientException::new))
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(ClientBadRequestException::new))
                .bodyToMono(GitRepoMemberClientResponse[].class)
                .flatMap(response -> {
                    if (response == null) {
                        return Mono.error(WebClientException::new);
                    }
                    return Mono.just(response);
                })
                .retryWhen(
                        Retry.fixedDelay(10, Duration.ofMillis(1500)))
                .filter(grm -> !Arrays.isNullOrEmpty(grm))
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
