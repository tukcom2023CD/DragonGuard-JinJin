package com.dragonguard.backend.domain.gitrepo.client;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.exception.WebClientRetryException;
import com.dragonguard.backend.global.template.client.GithubClient;
import com.dragonguard.backend.global.exception.ClientBadRequestException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

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
    private static final String PATH_FORMAT = "repos/%s/stats/contributors";
    private static final int MAX_ATTEMPTS = 10;
    private static final int DURATION_OF_MILLIS = 1500;
    private final WebClient webClient;

    @Override
    public List<GitRepoMemberClientResponse> requestToGithub(final GitRepoInfoRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(String.format(PATH_FORMAT, request.getName()))
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(HttpStatus.ACCEPTED::equals, response -> Mono.error(WebClientException::new))
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(ClientBadRequestException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.empty())
                .bodyToFlux(GitRepoMemberClientResponse.class)
                .collectList()
                .flatMap(this::validateResponse).retryWhen(getRetrySpec())
                .onErrorReturn(WebClientRetryException.class, List.of())
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }

    private RetryBackoffSpec getRetrySpec() {
        return Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofMillis(DURATION_OF_MILLIS))
                .filter(WebClientException.class::isInstance)
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new WebClientRetryException()));
    }

    private Mono<List<GitRepoMemberClientResponse>> validateResponse(final List<GitRepoMemberClientResponse> response) {
        if (isResponseEmpty(response)) {
            return Mono.error(WebClientException::new);
        }
        return Mono.just(response);
    }

    private boolean isResponseEmpty(final List<GitRepoMemberClientResponse> response) {
        return response == null || response.isEmpty() || response.stream()
                .anyMatch(g -> g.getTotal() == null || g.getWeeks() == null || g.getWeeks().isEmpty()
                        || g.getAuthor() == null || g.getAuthor().getLogin() == null || g.getAuthor().getAvatarUrl() == null);
    }
}
