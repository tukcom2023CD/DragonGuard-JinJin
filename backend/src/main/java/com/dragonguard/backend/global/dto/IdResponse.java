package com.dragonguard.backend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description id만 response 할 때 쓰이는 dto
 */
@Getter
@AllArgsConstructor
public class IdResponse<T> {
    private T id;
}
