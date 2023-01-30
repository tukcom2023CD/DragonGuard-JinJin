package com.dragonguard.backend.search.entity;

import com.dragonguard.backend.global.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@RedisHash(value = "search")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search extends BaseTime implements Serializable {

    @Id
    private String id;

    @Indexed
    @Column(nullable = false)
    private String searchWord;

    @Indexed
    @Column(nullable = false)
    private SearchType searchType;

    @Indexed
    private Integer page;

    @Indexed
    private List<String> resultIds = new ArrayList<>();

    @Builder
    public Search(String searchWord, SearchType searchType, Integer page) {
        this.searchWord = searchWord;
        this.searchType = searchType;
        this.page = page;
    }
}
