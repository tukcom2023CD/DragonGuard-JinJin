package com.dragonguard.backend.domain.member.client;

import com.dragonguard.backend.domain.member.dto.client.MemberClientRequest;
import com.dragonguard.backend.domain.member.dto.client.OrganizationRepoResponse;
import com.dragonguard.backend.global.exception.WebClientException;
import com.dragonguard.backend.global.template.client.GithubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

/**
 * @author 김승진
 * @description WebClient로 조직의 레포지토리를 가져오는 클라이언트
 */
@Component
@RequiredArgsConstructor
public class OrganizationRepoClient
        implements GithubClient<MemberClientRequest, OrganizationRepoResponse[]> {
    private static final String PATH_FORMAT = "orgs/%s/repos";
    private final WebClient webClient;

    @Override
    public OrganizationRepoResponse[] requestToGithub(final MemberClientRequest request) {
        return webClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path(String.format(PATH_FORMAT, request.getGithubId()))
                                        .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(OrganizationRepoResponse[].class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
