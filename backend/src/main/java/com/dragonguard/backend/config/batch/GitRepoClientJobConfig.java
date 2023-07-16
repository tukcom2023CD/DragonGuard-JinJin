package com.dragonguard.backend.config.batch;

import com.dragonguard.backend.config.batch.dto.GitRepoBatchRequest;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.ClientBadRequestException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


/**
 * @author 김승진
 * @description GitRepo 관련 배치 처리의 단위인 Job을 명시하는 클래스
 */

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class GitRepoClientJobConfig {
    private final GithubClient<GitRepoBatchRequest, Mono<List<GitRepoMember>>> gitRepoMemberBatchClient;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AdminApiToken adminApiToken;
    private final GitRepoReader gitRepoReader;
    private final CustomWriter customWriter;

    @Bean
    public Job clientJob() {
        return jobBuilderFactory.get("clientJob")
                .start(step())
                .preventRestart()
                .build();
    }

    @Bean
    @JobScope
    public Step step() {
        return stepBuilderFactory.get("step")
                .<GitRepo, List<GitRepoMember>>chunk(1)
                .reader(reader())
                .processor(compositeProcessor())
                .faultTolerant()
                .retry(WebClientException.class)
                .retry(WebClientResponseException.class)
                .retry(ClientBadRequestException.class)
                .retryLimit(2)
                .writer(writer())
                .faultTolerant()
                .build();
    }

    @Bean
    public CompositeItemProcessor<GitRepo, List<GitRepoMember>> compositeProcessor() {
        CompositeItemProcessor<GitRepo, List<GitRepoMember>> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(clientProcessor(), monoProcessor()));
        return processor;
    }

    @Bean
    public FunctionItemProcessor<GitRepo, Mono<List<GitRepoMember>>> clientProcessor() {
        return new FunctionItemProcessor<>(gitRepo -> gitRepoMemberBatchClient.requestToGithub(new GitRepoBatchRequest(adminApiToken.getApiToken(), gitRepo)));
    }

    @Bean
    public FunctionItemProcessor<Mono<List<GitRepoMember>>, List<GitRepoMember>> monoProcessor() {
        return new FunctionItemProcessor<>(mono -> mono.blockOptional().orElseGet(List::of));
    }

    @Bean
    public CustomWriter writer() {
        return customWriter;
    }

    @Bean
    @StepScope
    public GitRepoReader reader() {
        return gitRepoReader;
    }
}
