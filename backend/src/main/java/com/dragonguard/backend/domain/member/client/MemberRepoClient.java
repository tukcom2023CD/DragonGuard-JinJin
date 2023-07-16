package com.dragonguard.backend.domain.member.client;

import com.dragonguard.backend.domain.member.dto.client.MemberClientRequest;
import com.dragonguard.backend.domain.member.dto.client.MemberRepoResponse;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

/**
 * @author 김승진
 * @description WebClient로 멤버의 레포지토리를 가져오는 클라이언트
 */

@Component
@RequiredArgsConstructor
public class MemberRepoClient implements GithubClient<MemberClientRequest, MemberRepoResponse[]> {
    private final WebClient webClient;

    @Override
    public MemberRepoResponse[] requestToGithub(MemberClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("users/")
                                .path(request.getGithubId())
                                .path("/repos")
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MemberRepoResponse[].class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
