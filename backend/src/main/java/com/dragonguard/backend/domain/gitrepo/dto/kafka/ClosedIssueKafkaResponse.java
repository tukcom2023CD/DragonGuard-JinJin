package com.dragonguard.backend.domain.gitrepo.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClosedIssueKafkaResponse {
    private String name;
    private Integer closedIssue;
}
