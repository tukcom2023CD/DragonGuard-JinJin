package com.dragonguard.backend.config.converter;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;

public interface OrderConverter {
    OrderSpecifier<?>[] convert(Sort sort);
}
