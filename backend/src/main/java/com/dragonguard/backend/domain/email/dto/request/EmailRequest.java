package com.dragonguard.backend.domain.email.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    @NotNull
    private Long id;
    @Max(99999)
    @Min(10000)
    private Integer code;
}
