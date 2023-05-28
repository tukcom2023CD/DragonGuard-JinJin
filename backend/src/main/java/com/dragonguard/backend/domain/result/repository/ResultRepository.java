package com.dragonguard.backend.domain.result.repository;

import com.dragonguard.backend.domain.result.entity.Result;

import java.util.List;

public interface ResultRepository {
    List<Result> findAllBySearchId(Long searchId);

    boolean existsByNameAndSearchId(String name, Long searchId);

    Result save(Result result);
}
