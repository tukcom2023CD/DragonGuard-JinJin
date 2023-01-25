package com.dragonguard.backend.Result.entity;

import com.dragonguard.backend.global.BaseTime;
import com.dragonguard.backend.search.entity.SearchResult;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "result")
    private List<SearchResult> searchResults = new ArrayList<>();

    @Embedded
    private BaseTime baseTime;

    @Builder
    public Result(String name, List<SearchResult> searchResults) {
        this.name = name;
        this.searchResults = searchResults;
        this.baseTime = new BaseTime();
    }

    public void organizeSearchResult(SearchResult searchResult) {
        searchResult.organizeResult(this);
        this.searchResults.add(searchResult);
    }
}
