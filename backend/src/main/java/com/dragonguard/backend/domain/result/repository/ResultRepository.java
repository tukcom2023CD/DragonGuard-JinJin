package com.dragonguard.backend.domain.result.repository;

import com.dragonguard.backend.domain.result.entity.Result;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 검색 결과에 대한 DB 접근 로직을 갖는 인터페이스
 */
public interface ResultRepository {
    List<Result> findAllBySearchId(final Long searchId);

    boolean existsByNameAndSearchId(final String name, final Long searchId);

    Result save(final Result result);

    Optional<Result> findById(final Long id);
}
