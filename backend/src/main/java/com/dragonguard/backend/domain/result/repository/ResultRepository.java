package com.dragonguard.backend.domain.result.repository;

import com.dragonguard.backend.domain.result.entity.Result;

import java.util.List;
import java.util.Optional;

public interface ResultRepository {
    List<Result> findAllBySearchId(Long searchId);
    boolean existsByNameAndSearchId(String name, Long searchId);
    Result save(Result result);
    Optional<Result> findById(Long id);
    void deleteAllBySearchId(Long searchId);
}
