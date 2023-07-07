package com.dragonguard.backend.domain.search.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 필터 정보를 담는 DB Entity
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Filter implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String filter;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Search search;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Filter(String filter) {
        this.filter = filter;
    }

    public void organizeSearch(Search search) {
        this.search = search;
    }
}
