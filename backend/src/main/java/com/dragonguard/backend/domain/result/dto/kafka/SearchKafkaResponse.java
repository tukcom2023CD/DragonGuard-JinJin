package com.dragonguard.backend.domain.result.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchKafkaResponse {
    private String name;
    private String type;
    private Integer page;
}
