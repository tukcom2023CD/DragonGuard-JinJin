package com.dragonguard.backend.organization.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberRequest {
    @NotNull
    private Long organizationId;
}
