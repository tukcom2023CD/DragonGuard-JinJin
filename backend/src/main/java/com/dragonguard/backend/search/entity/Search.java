package com.dragonguard.backend.search.entity;

import com.dragonguard.backend.global.BaseTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RedisHash(value = "search")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}
