package com.dragonguard.backend.global.template.service;

/**
 * @author 김승진
 * @description 엔티티를 로드 하는 서비스 계층의 공통 로직을 뽑아낸 인터페이스
 */

public interface EntityLoader<T, ID> {
    T loadEntity(final ID id);
}
