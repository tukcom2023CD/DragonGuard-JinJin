package com.dragonguard.backend.domain.search.repository;

import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.entity.SearchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 김승진
 * @description 검색 정보를 DB에 CRUD를 요청하는 클래스
 */

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    List<Search> findByNameAndTypeAndPage(String name, SearchType type, Integer page);
}
