package com.dragonguard.backend.domain.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 이메일의 코드가 일치하는지 확인하는 정보를 담는 dto 클래스
 */

@Getter
@AllArgsConstructor
public class CheckCodeResponse {
    private Boolean isValidCode;
}
