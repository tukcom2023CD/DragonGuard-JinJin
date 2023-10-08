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
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeadLetter implements Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String topicName;

    private String keyName;

    private Integer partitionId;

    private Long offsetNumber;

    @Lob
    @Column(nullable = false)
    private String valueObject;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public DeadLetter(final String topicName, final String keyName, final Integer partitionId, final Long offsetNumber, final String valueObject) {
        this.topicName = topicName;
        this.keyName = keyName;
        this.partitionId = partitionId;
        this.offsetNumber = offsetNumber;
        this.valueObject = valueObject;
    }
}
