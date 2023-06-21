package com.dragonguard.backend.domain.member.client;

import com.dragonguard.backend.domain.member.dto.client.MemberClientRequest;
import com.dragonguard.backend.domain.member.dto.client.MemberOrganizationResponse;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

/**
 * @author 김승진
 * @description WebClient로 멤버의 조직을 가져오는 클라이언트
 */

@Component
@RequiredArgsConstructor
public class MemberOrganizationClient implements GithubClient<MemberClientRequest, MemberOrganizationResponse[]> {
    private final WebClient webClient;

    @Override
    public MemberOrganizationResponse[] requestToGithub(MemberClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("users/")
                                .path(request.getGithubId())
                                .path("/orgs")
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MemberOrganizationResponse[].class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
