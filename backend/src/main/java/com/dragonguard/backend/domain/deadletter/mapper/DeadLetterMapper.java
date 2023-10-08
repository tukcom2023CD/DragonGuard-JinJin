package com.dragonguard.backend.domain.deadletter.mapper;

import com.dragonguard.backend.domain.deadletter.entity.DeadLetter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter 엔티티 매핑 클래스
 */


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeadLetterMapper {

    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "key", source = "key")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "errorMessage", source = "errorMessage")
    DeadLetter toEntity(final String topic, final String key, final int partitionId, final Long offsetNumber, final String value, final String errorMessage);
}
