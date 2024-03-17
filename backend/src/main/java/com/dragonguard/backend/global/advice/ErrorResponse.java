package com.dragonguard.backend.global.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 에러에 대한 정보를 담는 dto 클래스
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}
