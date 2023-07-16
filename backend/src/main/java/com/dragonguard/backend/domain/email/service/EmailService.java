package com.dragonguard.backend.domain.email.service;

import com.dragonguard.backend.global.dto.IdResponse;

public interface EmailService {
    IdResponse<Long> sendAndSaveEmail();
    void deleteCode(final Long id);
    void sendEmail(final String memberEmail, final int random);
}
