package com.dragonguard.backend.domain.result.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchKafkaResponse {
    private String name;
    private String type;
    private Integer page;
}
