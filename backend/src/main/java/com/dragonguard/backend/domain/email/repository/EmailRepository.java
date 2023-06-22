package com.dragonguard.backend.domain.email.repository;

import com.dragonguard.backend.domain.email.entity.Email;

import java.util.Optional;

/**
 * @author 김승진
 * @description 이메일 정보를 DB와 상호작용할 때 필요한 로직을 갖는 인터페이스
 */

public interface EmailRepository {
    Email save(Email email);
    Optional<Email> findById(Long id);
}
