package com.dragonguard.backend.domain.email.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    @NotNull
    private Long id;

    @Range(min = 10000, max = 99999)
    private Integer code;
}
