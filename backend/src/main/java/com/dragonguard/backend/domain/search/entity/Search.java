package com.dragonguard.backend.domain.search.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 검색 정보를 담는 DB Entity
 */
@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search implements Auditable {
    @OneToMany(mappedBy = "search", cascade = CascadeType.PERSIST)
    private final List<Filter> filters = new ArrayList<>();

    @Id @GeneratedValue private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private SearchType type;

    private Integer page;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Search(
            final String name,
            final SearchType type,
            final Integer page,
            final List<Filter> filters) {
        this.name = name;
        this.type = type;
        this.page = page;
        organizeFilter(filters);
    }

    private void organizeFilter(final List<Filter> filters) {
        filters.forEach(
                filter -> {
                    filter.organizeSearch(this);
                    this.filters.add(filter);
                });
    }
}
