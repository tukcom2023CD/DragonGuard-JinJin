package com.dragonguard.backend.email.entity;

import com.dragonguard.backend.global.BaseTime;
import com.dragonguard.backend.global.SoftDelete;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private Integer code;

    @Builder
    public Email(UUID memberId, Integer code) {
        this.memberId = memberId;
        this.code = code;
    }
}
