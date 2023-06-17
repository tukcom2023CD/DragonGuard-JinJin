package com.dragonguard.backend.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class AsyncJobLauncherConfig {
    private final BasicBatchConfigurer basicBatchConfigurer;
    @Bean
    public SimpleJobLauncher simpleJobLauncher() {
        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }
}
