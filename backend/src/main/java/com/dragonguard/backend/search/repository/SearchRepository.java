package com.dragonguard.backend.search.repository;

import com.dragonguard.backend.search.entity.Search;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends CrudRepository<Search, String>, QueryByExampleExecutor<Search> {
}
