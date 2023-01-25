package com.dragonguard.backend.Result.repository;

import com.dragonguard.backend.Result.entity.QResult;
import com.dragonguard.backend.Result.entity.Result;
import com.dragonguard.backend.search.entity.GitRepoSortType;
import com.dragonguard.backend.search.entity.SortDirection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dragonguard.backend.Result.entity.QResult.result;

@Repository
public class ResultQueryRepository {
    private final JPAQueryFactory queryFactory;
    public ResultQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Result> findAllRepositories(
            String searchWord, GitRepoSortType gitRepoSortType,
            SortDirection sortDirection, Pageable pageable) {
        return queryFactory
                .selectFrom(result)
                .where(result.name.likeIgnoreCase(searchWord))
                .orderBy(sortDirection.getSortClassifier().apply(gitRepoSortType.getPath()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
