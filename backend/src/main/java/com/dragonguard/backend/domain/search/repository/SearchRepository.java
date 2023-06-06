package com.dragonguard.backend.domain.search.repository;

import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.entity.SearchType;

import java.util.List;
import java.util.Optional;

public interface SearchRepository {
    List<Search> findByNameAndTypeAndPage(String name, SearchType type, Integer page);
    Search save(Search search);
    Optional<Search> findById(Long id);
}
