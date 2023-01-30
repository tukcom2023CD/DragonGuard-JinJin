package com.dragonguard.backend.search.controller;

import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<ResultResponse>> getSearchResult(SearchRequest searchRequest) {
        return ResponseEntity.ok(searchService.getSearchResult(searchRequest));
    }
}
