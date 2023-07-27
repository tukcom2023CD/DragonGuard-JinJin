package com.dragonguard.backend.batch;

import com.dragonguard.backend.batch.dto.GitRepoBatchRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.Week;
import com.dragonguard.backend.domain.gitrepo.exception.WebClientRetryException;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoContribution;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GitRepoMemberBatchClient implements GithubClient<GitRepoBatchRequest, Mono<List<GitRepoMember>>> {
    private final WebClient webClient;
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final MemberMapper memberMapper;
    private final GitRepoRepository gitRepoRepository;
    private final MemberRepository memberRepository;

    @Override
    public Mono<List<GitRepoMember>> requestToGithub(final GitRepoBatchRequest request) {
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
                .bodyToFlux(GitRepoMemberClientResponse.class)
                .collectList()
                .flatMap(response -> {
                    if (response == null || response.isEmpty() || response.stream()
                            .anyMatch(g -> g.getTotal() == null || g.getWeeks() == null || g.getWeeks().isEmpty()
                                    || g.getAuthor() == null || g.getAuthor().getLogin() == null || g.getAuthor().getAvatarUrl() == null)) {
                        return Mono.error(WebClientException::new);
                    }
                    return Mono.just(response);
                })
                .retryWhen(
                        Retry.fixedDelay(10, Duration.ofMillis(1500))
                                .filter(WebClientException.class::isInstance)
                                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new WebClientRetryException())))
                .onErrorReturn(WebClientRetryException.class, List.of())
                .mapNotNull(result -> {
                    Set<GitRepoMember> gitRepoMembers = request.getGitRepo().getGitRepoMembers();

                    return result.stream().map(r -> {
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

                                    List<Week> weeks = r.getWeeks();

                                    return gitRepoMemberMapper.toEntity(
                                            memberRepository.findByGithubIdWithGitRepoMember(githubId).orElseGet(() -> memberMapper.toEntity(githubId, Role.ROLE_USER, AuthStep.NONE)),
                                            gitRepoRepository.findByIdWithGitRepoMember(request.getGitRepo().getId()).orElseThrow(EntityNotFoundException::new),
                                            new GitRepoContribution(r.getTotal(),
                                                    weeks.stream().mapToInt(Week::getA).sum(),
                                                    weeks.stream().mapToInt(Week::getD).sum()));

                                })).orElseGet(() -> {
                            return gitRepoMembers.stream()
                                    .filter(grm -> grm != null && grm.getMember().getGithubId().equals(r.getAuthor().getLogin()))
                                    .findFirst()
                                    .map(gitRepoMember -> {
                                        List<Week> weeks = r.getWeeks();
                                        gitRepoMember.updateGitRepoContribution(
                                                r.getTotal(),
                                                weeks.stream().mapToInt(Week::getA).sum(),
                                                weeks.stream().mapToInt(Week::getD).sum());
                                        return gitRepoMember;
                                    }).orElse(null);
                        });
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                }).filter(Objects::nonNull);
    }
}
