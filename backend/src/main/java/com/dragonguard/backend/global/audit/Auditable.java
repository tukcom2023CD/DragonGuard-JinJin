package com.dragonguard.backend.global.audit;

import java.time.LocalDateTime;

/**
 * @author 김승진
 * @description 저장, 수정, 삭제 시각을 저장할 엔티티들이 상속받을 인터페이스
 */

public interface Auditable {
    BaseTime getBaseTime();
    void setBaseTime(final BaseTime baseTime);

    default void delete() {
        this.getBaseTime().setDeletedAt(LocalDateTime.now());
    }
}
