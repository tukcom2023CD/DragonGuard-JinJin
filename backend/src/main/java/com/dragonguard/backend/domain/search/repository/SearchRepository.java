package com.dragonguard.backend.domain.search.repository;

import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.entity.SearchType;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 검색 정보에 대한 DB 접근 로직을 갖는 인터페이스
 */

public interface SearchRepository {
    List<Search> findByNameAndTypeAndPage(final String name, final SearchType type, final Integer page);
    Search save(final Search search);
    Optional<Search> findById(final Long id);
}
