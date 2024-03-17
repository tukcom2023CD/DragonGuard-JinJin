package com.dragonguard.backend.batch;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 김승진
 * @description GitRepo 관련 배치 처리를 수행하는 클래스
 */
@Configuration
@RequiredArgsConstructor
public class JobScheduler {
    private static final String MAP_KEY_NOW = "now";
    private final JobLauncher jobLauncher;
    private final Job clientJob;

    public void launchJob() throws Exception {
        final Map<String, JobParameter> jobParametersMap = new HashMap<>();
        jobParametersMap.put(MAP_KEY_NOW, new JobParameter(LocalDateTime.now().toString()));
        jobLauncher.run(clientJob, new JobParameters(jobParametersMap));
    }
}
