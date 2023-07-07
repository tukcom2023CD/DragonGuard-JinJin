package com.dragonguard.backend.domain.email.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author 김승진
 * @description 이메일의 정보를 담는 도메인 엔티티 클래스
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private Integer code;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Email(UUID memberId, Integer code) {
        this.memberId = memberId;
        this.code = code;
    }
}
