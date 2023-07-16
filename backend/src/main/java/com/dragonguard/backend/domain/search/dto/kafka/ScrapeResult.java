package com.dragonguard.backend.domain.search.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 스크래핑을 통한 검색 결과의 응답 정보를 갖는 dto 클래스
 */

@Getter
@AllArgsConstructor
public class ScrapeResult {
    private String full_name;
}
