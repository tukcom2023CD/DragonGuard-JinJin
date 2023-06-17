package com.dragonguard.backend.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class AsyncJobLauncherConfig {
    private final BasicBatchConfigurer basicBatchConfigurer;
    @Bean
    @Scheduled(cron = "0 50 1,3,5,7,9,11,13,15,17,19,21,23 * * *", zone = "Asia/Seoul")
    public SimpleJobLauncher simpleJobLauncher() {
        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }
}
