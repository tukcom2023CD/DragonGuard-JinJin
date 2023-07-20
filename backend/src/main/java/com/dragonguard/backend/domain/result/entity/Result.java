package com.dragonguard.backend.domain.result.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 검색 결과를 담는 Entity
 */

@Getter
@Entity
@EqualsAndHashCode(of = "name")
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long searchId;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Result(String name, Long searchId) {
        this.name = name;
        this.searchId = searchId;
    }
}
