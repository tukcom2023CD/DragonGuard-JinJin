package com.dragonguard.backend.config.batch;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.Week;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.ClientBadRequestException;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.exception.NoApiTokenException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author 김승진
 * @description GitRepo 관련 배치 처리의 단위인 Job을 명시하는 클래스
 */

@Configuration
@RequiredArgsConstructor
public class WebClientJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final GithubClient<GitRepoInfoRequest, GitRepoMemberClientResponse[]> gitRepoMemberClient;
    private final AdminApiToken adminApiToken;

    @Bean
    public Job clientJob() {
        return jobBuilderFactory.get("clientJob")
                .start(step())
                .build();
    }

    @Bean
    @JobScope
    public Step step() {
        return stepBuilderFactory.get("step")
                .<GitRepo, Set<GitRepoMember>>chunk(10)
                .reader(reader())
                .processor(processor())
                .faultTolerant()
                .retry(WebClientException.class)
                .retry(WebClientResponseException.class)
                .retry(ClientBadRequestException.class)
                .noRetry(NoApiTokenException.class)
                .noRetry(EntityNotFoundException.class)
                .retryLimit(5)
                .writer(writer())
                .faultTolerant()
                .skip(DataIntegrityViolationException.class)
                .skip(ConstraintViolationException.class)
                .build();
    }

    @Bean
    @StepScope
    public FunctionItemProcessor<GitRepo, Set<GitRepoMember>> processor() {
        String apiToken = adminApiToken.getApiToken();

        return new FunctionItemProcessor<>(gitRepo -> {
                List<GitRepoMemberClientResponse> list = Arrays.asList(gitRepoMemberClient.requestToGithub(new GitRepoInfoRequest(apiToken, gitRepo.getName(), LocalDateTime.now().getYear())));
                return getGitRepoMembers(gitRepo, list);
        });
    }

    @Bean
    @StepScope
    public JpaItemWriter<Set<GitRepoMember>> writer() {
        return new JpaItemWriterBuilder<Set<GitRepoMember>>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<GitRepo> reader() {
        return new JpaPagingItemReaderBuilder<GitRepo>()
                .pageSize(10)
                .queryString("SELECT gr FROM GitRepo gr ORDER BY gr.id ASC")
                .entityManagerFactory(entityManagerFactory)
                .name("jpaPagingReader")
                .build();
    }

    private Set<GitRepoMember> getGitRepoMembers(GitRepo gitRepo, List<GitRepoMemberClientResponse> list) {
        return gitRepo.getGitRepoMembers().stream()
                .map(gitRepoMember -> {
                    GitRepoMemberClientResponse gitRepoMemberResponse = list.stream()
                            .filter(response -> gitRepoMember.getMember().getName().equals(response.getAuthor().getLogin()))
                            .findFirst()
                            .orElseThrow(EntityNotFoundException::new);

                    List<Week> weeks = Arrays.asList(gitRepoMemberResponse.getWeeks());

                    gitRepoMember.updateGitRepoContribution(
                            gitRepoMemberResponse.getTotal(),
                            weeks.stream().mapToInt(Week::getA).sum(),
                            weeks.stream().mapToInt(Week::getD).sum());

                    return gitRepoMember;
                }).collect(Collectors.toSet());
    }
}
