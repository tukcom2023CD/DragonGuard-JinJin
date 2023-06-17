package com.dragonguard.backend.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
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
    private final SimpleJobLauncher simpleJobLauncher;
    private final Job clientJob;

    @Scheduled(cron = "0 50 1,3,5,7,9,11,13,15,17,19,21,23 * * *", zone = "Asia/Seoul")
    public void launchJob() throws Exception {
        Map<String, JobParameter> jobParametersMap = new HashMap<>();
        jobParametersMap.put("now", new JobParameter(LocalDateTime.now().toString()));
        JobParameters parameters = new JobParameters(jobParametersMap);
        simpleJobLauncher.run(clientJob, parameters);
    }
}
