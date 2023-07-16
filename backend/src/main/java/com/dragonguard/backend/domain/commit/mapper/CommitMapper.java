package com.dragonguard.backend.domain.commit.mapper;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.global.mapper.ContributionEntityMapper;
import org.mapstruct.Mapper;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 커밋내역 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface CommitMapper extends ContributionEntityMapper<Commit> {}
