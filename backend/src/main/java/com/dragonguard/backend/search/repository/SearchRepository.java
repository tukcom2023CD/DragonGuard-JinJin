package com.dragonguard.backend.search.repository;

import com.dragonguard.backend.search.entity.Search;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 검색 정보를 Redis에 저장 및 조회를 요청하는 클래스
 */

@Repository
public interface SearchRepository extends CrudRepository<Search, String>, QueryByExampleExecutor<Search> {
}
