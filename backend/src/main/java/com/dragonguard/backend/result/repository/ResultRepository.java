package com.dragonguard.backend.result.repository;

import com.dragonguard.backend.result.entity.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
    List<Result> findAllBySearchId(String searchId);
}
