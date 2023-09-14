package com.dragonguard.backend.domain.member.client;

import com.dragonguard.backend.domain.member.dto.client.MemberClientRequest;
import com.dragonguard.backend.domain.member.dto.client.MemberCodeReviewResponse;
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
 * @description WebClient로 멤버의 코드리뷰를 가져오는 클라이언트
 */

@Component
@RequiredArgsConstructor
public class MemberCodeReviewClient implements GithubClient<MemberClientRequest, MemberCodeReviewResponse> {
    private static final String PATH_FORMAT = "search/issues?q=reviewed-by:%s+type:pr+created:%d-01-01..%s";
    private final WebClient webClient;

    @Override
    public MemberCodeReviewResponse requestToGithub(final MemberClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(String.format(PATH_FORMAT, request.getGithubId(), request.getYear(), LocalDate.now()))
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MemberCodeReviewResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
