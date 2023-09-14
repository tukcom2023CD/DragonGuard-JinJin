package com.dragonguard.backend.domain.result.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import lombok.*;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 검색 결과를 담는 Entity
 */

@Getter
@Entity
@SoftDelete
@EqualsAndHashCode(of = "name")
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
    public Result(final String name, final Long searchId) {
        this.name = name;
        this.searchId = searchId;
    }
}
