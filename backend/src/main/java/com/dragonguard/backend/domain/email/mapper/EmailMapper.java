package com.dragonguard.backend.domain.email.mapper;

import com.dragonguard.backend.domain.email.entity.Email;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author 김승진
 * @description email 엔티티를 매핑해주는 mapper 클래스
 */

@Component
public class EmailMapper {
    public Email toEntity(final Integer code, final UUID memberId) {
        return Email.builder()
                .code(code)
                .memberId(memberId)
                .build();
    }
}
