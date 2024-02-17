package com.dragonguard.backend.domain.commit.mapper;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 커밋내역 Entity와 dto의 변환을 돕는 클래스
 */
@Mapper(
        componentModel = ComponentModel.SPRING,
        imports = {LocalDate.class})
public interface CommitMapper extends ContributionMapper<Commit> {}
