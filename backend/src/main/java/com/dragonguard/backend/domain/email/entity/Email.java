package com.dragonguard.backend.domain.email.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author 김승진
 * @description 이메일의 정보를 담는 도메인 엔티티 클래스
 */
@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email implements Auditable {
    @Id @GeneratedValue private Long id;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private Integer code;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Email(final UUID memberId, final Integer code) {
        this.memberId = memberId;
        this.code = code;
    }

    public boolean notMatchCode(final int code) {
        return this.code != code;
    }
}
