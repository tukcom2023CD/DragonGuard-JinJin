package com.dragonguard.backend.global.audit;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * @author 김승진
 * @description 생성, 수정, 삭제 시간을 저장할 엔티티에 들어갈 공통 embeddable 클래스
 */

@Getter
@Setter
@Embeddable
public class BaseTime {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
