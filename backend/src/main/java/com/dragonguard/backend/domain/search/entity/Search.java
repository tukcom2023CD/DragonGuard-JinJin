package com.dragonguard.backend.domain.search.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
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
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search implements Auditable {
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

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Search(String name, SearchType type, Integer page, List<Filter> filters) {
        this.name = name;
        this.type = type;
        this.page = page;
        filters.forEach(filter -> {
            filter.organizeSearch(this);
            this.filters.add(filter);
        });
    }
}
