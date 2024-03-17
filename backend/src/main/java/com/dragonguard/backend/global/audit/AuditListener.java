package com.dragonguard.backend.global.audit;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @author 김승진
 * @description 저장, 수정, 삭제 시각을 저장할 로직
 */
public class AuditListener {
    @PrePersist
    public void setCreatedAt(final Auditable auditable) {
        BaseTime baseTime = Optional.ofNullable(auditable.getBaseTime()).orElseGet(BaseTime::new);
        baseTime.setCreatedAt(LocalDateTime.now());
        auditable.setBaseTime(baseTime);
    }

    @PreUpdate
    public void setUpdatedAt(final Auditable auditable) {
        BaseTime baseTime = auditable.getBaseTime();
        baseTime.setUpdatedAt(LocalDateTime.now());
    }
}
