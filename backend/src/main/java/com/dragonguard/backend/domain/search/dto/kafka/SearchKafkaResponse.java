package com.dragonguard.backend.domain.search.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 카프카를 통한 검색 응답 정보를 갖는 dto 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchKafkaResponse {
    private String name;
    private String type;
    private Integer page;
}
