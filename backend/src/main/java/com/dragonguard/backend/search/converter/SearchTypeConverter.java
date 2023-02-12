package com.dragonguard.backend.search.converter;

import com.dragonguard.backend.search.entity.SearchType;
import org.springframework.core.convert.converter.Converter;

public class SearchTypeConverter  implements Converter<String, SearchType> {
    @Override
    public SearchType convert(String source) {
        return SearchType.valueOf(source.toUpperCase());
    }
}