package com.dragonguard.backend.domain.search.repository;

import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.global.repository.EntityRepository;

/**
 * @author 김승진
 * @description 검색 정보를 DB에 CRUD를 요청하는 클래스
 */

public interface JpaSearchRepository extends EntityRepository<Search, Long>, SearchRepository {}
