package com.dragonguard.backend.domain.result.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultKafkaResponse {
    private List<ResultDetailsResponse> result;
    private SearchKafkaResponse search;
}
