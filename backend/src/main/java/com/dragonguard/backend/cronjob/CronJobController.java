package com.dragonguard.backend.cronjob;

import com.dragonguard.backend.batch.JobScheduler;
import com.dragonguard.backend.domain.blockchain.client.KlaytnFaucetClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 김승진
 * @description k8s cronjob 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/cronjob")
public class CronJobController {

    private final JobScheduler jobScheduler;
    private final KlaytnFaucetClient klaytnFaucetClient;

    @PostMapping("/git-repos")
    public ResponseEntity<Void> gitRepoBatch() throws Exception {
        jobScheduler.launchJob();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/klaytn-faucet")
    public ResponseEntity<Void> requestKlaytnFaucet() {
        klaytnFaucetClient.requestKlaytnFaucet();
        return ResponseEntity.ok().build();
    }
}
