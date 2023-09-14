package com.dragonguard.backend.domain.email.service;

import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.global.dto.IdResponse;
import com.dragonguard.backend.global.service.EntityLoader;

public interface EmailService extends EntityLoader<Email, Long> {
    IdResponse<Long> sendAndSaveEmail();
    void deleteCode(final Long id);
}
