package com.dragonguard.backend.domain.contribution.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContributionKafkaResponse {
    private String githubId;
    private String name;
    private Integer contribution;
    private String profileImage;
}
