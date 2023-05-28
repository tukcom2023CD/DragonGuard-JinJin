package com.dragonguard.backend.domain.email.repository;

import com.dragonguard.backend.domain.email.entity.Email;

import java.util.Optional;

public interface EmailRepository {
    Email save(Email email);

    Optional<Email> findById(Long id);
}
