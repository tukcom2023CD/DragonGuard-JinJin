package com.dragonguard.backend.domain.search.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 김승진
 * @description kafka의 응답 정보를 갖는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultKafkaResponse {
    private List<ResultDetailsResponse> result;
    private SearchKafkaResponse search;
}
