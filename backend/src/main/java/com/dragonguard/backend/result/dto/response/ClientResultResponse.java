package com.dragonguard.backend.result.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 김승진
 * @description 검색 결과를 Github REST API에서 받아오는 dto
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResultResponse {
    private String full_name;
}
