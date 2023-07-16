package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.global.converter.OrderConverter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.dragonguard.backend.domain.member.entity.QMember.member;

/**
 * @author 김승진
 * @description 멤버 정렬 조건을 설정하는 클래스
 */

@Component
public class MemberOrderConverter implements OrderConverter {

    private final Map<String, ComparableExpressionBase<?>> keywordMap = new HashMap<>();

    public MemberOrderConverter() {
        initializeMap();
    }

    private void initializeMap() {
        keywordMap.put("commits", member.sumOfCommits);
        keywordMap.put("tokens", member.sumOfTokens);
    }

    @Override
    public OrderSpecifier<?>[] convert(Sort sort) {
        return sort.stream()
                .map(
                        s -> {
                            ComparableExpressionBase<?> path = keywordMap.get(s.getProperty());
                            if (s.isAscending()) {
                                return path.asc();
                            }
                            return path.desc();
                        })
                .toArray(OrderSpecifier[]::new);
    }
}
