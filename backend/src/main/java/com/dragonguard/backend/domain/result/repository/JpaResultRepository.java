package com.dragonguard.backend.domain.result.repository;

import com.dragonguard.backend.domain.result.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 검색 결과를 DB에 저장 및 조회 요청을 하는 클래스
 */

@Repository
public interface JpaResultRepository extends JpaRepository<Result, Long>, ResultRepository {}
