package com.dragonguard.backend.search.entity;

import com.dragonguard.backend.global.basetime.BaseTime;
import com.dragonguard.backend.global.basetime.SoftDelete;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 김승진
 * @description 검색 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private SearchType type;

    private Integer page;

    @OneToMany(mappedBy = "search")
    private List<Filter> filters = new ArrayList<>();

    @Builder
    public Search(String name, SearchType type, Integer page, List<Filter> filters) {
        this.name = name;
        this.type = type;
        this.page = page;
        this.filters.forEach(filter -> filter.organizeSearch(this));
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }
}
