package com.dragonguard.backend.global.template.converter;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;

/**
 * @author 김승진
 * @description 여러 조건으로 정렬을 요구하는 API들의 Repository 계층에서 쓰일 인터페이스
 */

public interface OrderConverter {
    OrderSpecifier<?>[] convert(final Sort sort);
}
