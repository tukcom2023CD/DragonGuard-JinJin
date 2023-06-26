package com.dragonguard.backend.config.batch;

import com.dragonguard.backend.domain.gitrepo.dto.batch.GitRepoBatchRequest;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.ClientBadRequestException;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Disposable;
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
    private static final int POOL_SIZE = 10;

    @Bean
    public Job clientJob() {
        return jobBuilderFactory.get("clientJob")
                .start(step())
                .preventRestart()
                .build();
    }

    @Bean
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(POOL_SIZE);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

    @Bean
    @JobScope
    public Step step() {
        return stepBuilderFactory.get("step")
                .<GitRepo, Disposable>chunk(50)
                .reader(reader())
                .processor(compositeProcessor())
                .faultTolerant()
                .retry(WebClientException.class)
                .retry(WebClientResponseException.class)
                .retry(ClientBadRequestException.class)
                .noRetry(ConstraintViolationException.class)
                .noRetry(DataIntegrityViolationException.class)
                .retryLimit(2)
                .writer(writer())
                .taskExecutor(executor())
                .throttleLimit(POOL_SIZE)
                .build();
    }

    @Bean
    public CompositeItemProcessor<GitRepo, Disposable> compositeProcessor() {
        CompositeItemProcessor<GitRepo, Disposable> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(clientProcessor(), monoProcessor()));
        return processor;
    }

    @Bean
    public FunctionItemProcessor<GitRepo, Mono<List<GitRepoMember>>> clientProcessor() {
        return new FunctionItemProcessor<>(gitRepo -> gitRepoMemberBatchClient.requestToGithub(new GitRepoBatchRequest(adminApiToken.getApiToken(), gitRepo)));
    }

    @Bean
    public FunctionItemProcessor<Mono<List<GitRepoMember>>, Disposable> monoProcessor() {
        return new FunctionItemProcessor<>(Mono::subscribe);
    }

    @Bean
    public NoOpWriter<Disposable> writer() {
        return new NoOpWriter<>();
    }

    @Bean
    @StepScope
    public GitRepoReader reader() {
        return gitRepoReader;
    }
}
