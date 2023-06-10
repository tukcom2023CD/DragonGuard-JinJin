package com.dragonguard.backend.domain.result.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResultKafkaResponse {
    private List<ResultDetailsResponse> result;
    private SearchKafkaResponse search;
}
