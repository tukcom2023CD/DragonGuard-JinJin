package com.dragonguard.backend.search.entity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum SortDirection {
    ASC(ComparableExpressionBase::asc),
    DESC(ComparableExpressionBase::desc);

    private final Function<ComparableExpressionBase, OrderSpecifier> sortClassifier;
}
