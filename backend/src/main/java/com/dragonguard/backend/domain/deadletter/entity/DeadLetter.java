package com.dragonguard.backend.domain.deadletter.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter 엔티티
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeadLetter {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String topic;
    private String key;
    private Integer partitionId;
    private Long offset;
    private String value;
    private String errorMessage;
    private Boolean isRetried;

    @Builder
    public DeadLetter(final String topic, final String key, final Integer partitionId, final Long offset, final String value, final String errorMessage) {
        this.topic = topic;
        this.key = key;
        this.partitionId = partitionId;
        this.offset = offset;
        this.value = value;
        this.errorMessage = errorMessage;
        this.isRetried = false;
    }
}
