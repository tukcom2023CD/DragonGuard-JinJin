package com.dragonguard.backend.search.dto.response;

import com.dragonguard.backend.result.dto.request.ResultRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchClientResponse {
    private ResultRequest[] items;
}
