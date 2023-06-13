package com.dragonguard.backend.domain.contribution.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContributionKafkaResponse {
    private String githubId;
    private String name;
    private Integer contribution;
    private String profileImage;
}
