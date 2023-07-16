package com.dragonguard.backend.domain.email.mapper;

import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.global.mapper.EntityMapper;
import org.mapstruct.Mapper;

import java.util.UUID;

/**
 * @author 김승진
 * @description 이메일 정보 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface EmailMapper extends EntityMapper {
    Email toEntity(final Integer code, final UUID memberId);
}
