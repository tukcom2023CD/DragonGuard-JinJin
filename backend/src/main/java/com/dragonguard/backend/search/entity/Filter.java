package com.dragonguard.backend.search.entity;

import com.dragonguard.backend.global.basetime.SoftDelete;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 필터 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Filter {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String filter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filters")
    private Search search;

    @Builder
    public Filter(String filter, Search search) {
        this.filter = filter;
        this.search = search;
    }

    public void organizeSearch(Search search) {
        this.search = search;
        search.addFilter(this);
    }
}
