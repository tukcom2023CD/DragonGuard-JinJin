package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.config.converter.OrderConverter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.dragonguard.backend.member.entity.QMember.member;

@Component
public class MemberOrderConverter implements OrderConverter {

    private final Map<String, ComparableExpressionBase<?>> keywordMap = new HashMap<>();

    public MemberOrderConverter() {
        initializeMap();
    }

    private void initializeMap() {
        keywordMap.put("commits", member.commitsSum);
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
