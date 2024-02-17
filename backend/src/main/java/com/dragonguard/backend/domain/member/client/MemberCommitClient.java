package com.dragonguard.backend.domain.member.client;

import com.dragonguard.backend.domain.member.dto.client.MemberClientRequest;
import com.dragonguard.backend.domain.member.dto.client.MemberCommitResponse;
import com.dragonguard.backend.global.annotation.DistributedLock;
import com.dragonguard.backend.global.exception.WebClientException;
import com.dragonguard.backend.global.template.client.GithubClient;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author 김승진
 * @description WebClient로 멤버의 커밋을 가져오는 클라이언트
 */
@Component
@RequiredArgsConstructor
public class MemberCommitClient implements GithubClient<MemberClientRequest, MemberCommitResponse> {
    private static final String PATH_FORMAT =
            "search/commits?q=author:%s+committer-date:%%3E%d-01-01";
    private final WebClient webClient;

    @Override
    @DistributedLock(name = "#request.getGithubId().concat('memberCommitClient')")
    public MemberCommitResponse requestToGithub(final MemberClientRequest request) {
        return webClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path(
                                                String.format(
                                                        PATH_FORMAT,
                                                        request.getGithubId(),
                                                        request.getYear()))
                                        .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MemberCommitResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
