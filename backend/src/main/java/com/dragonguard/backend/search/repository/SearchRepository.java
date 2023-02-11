package com.dragonguard.backend.search.repository;

import com.dragonguard.backend.search.entity.Search;
import com.dragonguard.backend.search.entity.SearchType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchRepository extends CrudRepository<Search, String> {
    Optional<Search> findBySearchWordAndPageAndSearchType(String searchWord, Integer page, SearchType searchType);
}
