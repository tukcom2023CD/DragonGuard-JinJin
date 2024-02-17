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

    @Mapping(target = "topicName", source = "topicName")
    @Mapping(target = "keyName", source = "keyName")
    @Mapping(target = "valueObject", source = "valueObject")
    @Mapping(target = "groupId", source = "groupId")
    DeadLetter toEntity(
            final String topicName,
            final String keyName,
            final int partitionId,
            final Long offsetNumber,
            final String valueObject,
            final String groupId);
}
