package com.dragonguard.backend.search.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 김승진
 * @description 검색 정보를 담는 Redis Entity
 */

@Data
@RedisHash("search")
public class Search implements Serializable {
    @Id
    private String id;

    @Indexed
    @Column(nullable = false)
    private String searchWord;

    @Indexed
    @Enumerated(EnumType.STRING)
    private SearchType searchType;

    @Indexed
    private Integer page;

    private List<String> filters = new ArrayList<>();

    @Builder
    public Search(String id, String searchWord, SearchType searchType, Integer page, List<String> filters) {
        this.id = id;
        this.searchWord = searchWord;
        this.searchType = searchType;
        this.page = page;
        this.filters = filters;
    }
}
