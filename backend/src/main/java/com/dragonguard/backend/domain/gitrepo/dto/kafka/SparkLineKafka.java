package com.dragonguard.backend.domain.gitrepo.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SparkLineKafka {
    private String githubToken;
    private Long id;
}
