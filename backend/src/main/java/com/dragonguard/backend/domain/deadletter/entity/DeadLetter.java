package com.dragonguard.backend.domain.deadletter.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter 엔티티
 */

@Getter
@Entity
@SoftDelete
@Table(name = "deadletter") // 테이블 생성 오류 해결을 위함
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeadLetter implements Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String topic;

    private String key;

    private Integer partitionId;

    private Long offset;

    @Column(nullable = false)
    private String value;

    private String errorMessage;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public DeadLetter(final String topic, final String key, final Integer partitionId, final Long offset, final String value, final String errorMessage) {
        this.topic = topic;
        this.key = key;
        this.partitionId = partitionId;
        this.offset = offset;
        this.value = value;
        this.errorMessage = errorMessage;
    }
}
