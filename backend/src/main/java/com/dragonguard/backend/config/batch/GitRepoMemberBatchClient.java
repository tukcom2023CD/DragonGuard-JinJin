package com.dragonguard.backend.config.batch;

import com.dragonguard.backend.domain.gitrepo.dto.batch.GitRepoBatchRequest;
import com.dragonguard.backend.domain.gitrepo.dto.batch.GitRepoBatchResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.Week;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GitRepoMemberBatchClient implements GithubClient<GitRepoBatchRequest, Mono<List<GitRepoMember>>> {
    private final WebClient webClient;
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final MemberMapper memberMapper;
    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    public Mono<List<GitRepoMember>> requestToGithub(GitRepoBatchRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("repos/")
                                .path(request.getGitRepo().getName())
                                .path("/stats")
                                .path("/contributors")
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(hs -> hs.equals(HttpStatus.NO_CONTENT), response -> Mono.error(WebClientException::new))
                .bodyToMono(GitRepoBatchResponse[].class)
                .retryWhen(
                        Retry.fixedDelay(10, Duration.ofMillis(1500))
                                .filter(WebClientException.class::isInstance)
                                .filter(WebClientRequestException.class::isInstance)
                                .filter(Throwable.class::isInstance))
                .mapNotNull(result -> {
                    Set<GitRepoMember> gitRepoMembers = request.getGitRepo().getGitRepoMembers();

                    return Arrays.stream(result).map(r -> {
                        if (r.getAuthor() == null || !StringUtils.hasText(r.getAuthor().getLogin())) {
                            return null;
                        }

                        return Optional.ofNullable(gitRepoMembers.stream()
                                .filter(grm -> grm.getMember().getGithubId().equals(r.getAuthor().getLogin()))
                                .findFirst()
                                .orElseGet(() -> {
                                    String githubId = r.getAuthor().getLogin();
                                    if (memberRepository.existsByGithubId(githubId)) {
                                        Member member = memberRepository.findByGithubIdWithGitRepoMember(githubId).orElseThrow(EntityNotFoundException::new);
                                        return gitRepoMemberMapper.toEntity(member, request.getGitRepo());
                                    }

                                    try {
                                        Member member = memberRepository.save(memberMapper.toEntity(githubId, Role.ROLE_USER, AuthStep.NONE));
                                        return gitRepoMemberMapper.toEntity(member, request.getGitRepo());
                                    } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                                        return null;
                                    }

                                })).orElseGet(() -> {
                            return gitRepoMembers.stream()
                                    .filter(grm -> grm != null && grm.getMember().getGithubId().equals(r.getAuthor().getLogin()))
                                    .findFirst()
                                    .map(gitRepoMember -> {
                                        List<Week> weeks = Arrays.asList(r.getWeeks());
                                        gitRepoMember.updateGitRepoContribution(
                                                r.getTotal(),
                                                weeks.stream().mapToInt(Week::getA).sum(),
                                                weeks.stream().mapToInt(Week::getD).sum());
                                        return gitRepoMember;
                                    }).orElse(null);
                        });
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                }).filter(Objects::nonNull).map(gitRepoMemberRepository::saveAll);
    }
}
