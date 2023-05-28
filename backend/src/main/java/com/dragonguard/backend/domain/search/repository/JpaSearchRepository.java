package com.dragonguard.backend.domain.search.repository;

import com.dragonguard.backend.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 검색 정보를 DB에 CRUD를 요청하는 클래스
 */

@Repository
public interface JpaSearchRepository extends JpaRepository<Search, Long>, SearchRepository {
}
