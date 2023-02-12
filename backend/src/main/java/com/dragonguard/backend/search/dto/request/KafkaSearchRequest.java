package com.dragonguard.backend.search.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaSearchRequest {
    private String name;
    private String type;
    private Integer page;
}
