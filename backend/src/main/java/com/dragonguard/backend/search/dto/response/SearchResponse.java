package com.dragonguard.backend.search.dto.response;

import com.dragonguard.backend.Result.dto.response.ResultResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private Long id;
    private String searchWord;
    private List<ResultResponse> resultResponses = new ArrayList<>();
}
