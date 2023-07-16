package com.dragonguard.backend.domain.gitrepo.client;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.exception.WebClientRetryException;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.ClientBadRequestException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Repository 멤버의 기여도 정보를 Github REST API에 요청하는 클래스
 */

@Component
@RequiredArgsConstructor
public class GitRepoMemberClient implements GithubClient<GitRepoInfoRequest, List<GitRepoMemberClientResponse>> {
    private final WebClient webClient;

    @Override
    public List<GitRepoMemberClientResponse> requestToGithub(GitRepoInfoRequest request) {
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
                .onStatus(HttpStatus.NO_CONTENT::equals, response -> Mono.error(WebClientException::new))
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(ClientBadRequestException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.empty())
                .bodyToFlux(GitRepoMemberClientResponse.class)
                .collectList()
                .flatMap(response -> {
                    if (response == null || response.isEmpty() || response.stream()
                            .anyMatch(g -> g.getTotal() == null || g.getWeeks() == null || g.getWeeks().isEmpty()
                                    || g.getAuthor() == null || g.getAuthor().getLogin() == null || g.getAuthor().getAvatarUrl() == null)) {
                        return Mono.error(WebClientException::new);
                    }
                    return Mono.just(response);
                }).retryWhen(
                        Retry.fixedDelay(10, Duration.ofMillis(1500))
                                .filter(WebClientException.class::isInstance)
                                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new WebClientRetryException())))
                .onErrorReturn(WebClientRetryException.class, List.of())
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
