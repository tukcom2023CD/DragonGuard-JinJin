package com.dragonguard.backend.domain.member.client;

import com.dragonguard.backend.domain.member.dto.client.MemberClientRequest;
import com.dragonguard.backend.domain.member.dto.client.MemberPullRequestResponse;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * @author 김승진
 * @description WebClient로 멤버의 PullRequest 가져오는 클라이언트
 */

@Component
@RequiredArgsConstructor
public class MemberPullRequestClient implements GithubClient<MemberClientRequest, MemberPullRequestResponse> {
    private final WebClient webClient;

    @Override
    public MemberPullRequestResponse requestToGithub(MemberClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("search/issues?q=type:pr+author:")
                                .path(request.getGithubId() + "+created:" + request.getYear() + "-01-01.." + LocalDate.now())
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MemberPullRequestResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
