package com.dragonguard.backend.domain.batch.controller;

import com.dragonguard.backend.batch.JobScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batch")
public class BatchController {
    private final JobScheduler jobScheduler;

    /**
     * 배치 테스트용 컨트롤러입니다.
     */

    @PostMapping
    public ResponseEntity<Void> launchBatch() throws Exception {
        jobScheduler.launchJob();
        return ResponseEntity.ok().build();
    }
}
