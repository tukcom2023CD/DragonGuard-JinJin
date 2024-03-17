package com.dragonguard.backend.domain.email.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author 김승진
 * @description 이메일 관련 요청을 받아와 처리하는 Controller 클래스
 */
@Setter // ModelAttribute를 사용하기 위함
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    @NotNull private Long id;

    @Range(min = 10000, max = 99999)
    private Integer code;

    @NotNull private Long organizationId;
}
