package com.dragonguard.backend.search.entity;

import com.dragonguard.backend.Result.entity.Result;
import com.dragonguard.backend.global.BaseTime;
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
public class Search {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String searchWord;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SearchType searchType;

    @Column(nullable = false)
    private Integer searchPage;

    @OneToMany(mappedBy = "search")
    private List<SearchResult> searchResults = new ArrayList<>();

    @Embedded
    private BaseTime baseTime;

    @Builder
    public Search(String searchWord, SearchType searchType, Integer searchPage, List<SearchResult> searchResults) {
        this.searchWord = searchWord;
        this.searchType = searchType;
        this.searchPage = searchPage;
        this.searchResults = searchResults;
        this.baseTime = new BaseTime();
    }

    public void organizeSearchResults(SearchResult searchResult) {
        searchResult.organizeSearch(this);
        this.searchResults.add(searchResult);
    }
}
