package com.dragonguard.backend.domain.search.controller;

import com.dragonguard.backend.domain.result.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.result.dto.response.UserResultResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.dragonguard.backend.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 김승진
 * @description 검색에 대한 요청을 처리하는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping(params = "type=USERS")
    @Cacheable(value = "results", key = "{#name, page, filters}", cacheManager = "cacheManager")
    public ResponseEntity<List<UserResultResponse>> getUsersSearchResult(
            @RequestParam(value = "filters", required = false) List<String> filters,
            @RequestParam String name, @RequestParam Integer page) {
        return ResponseEntity.ok(searchService.getUserSearchResultByClient(new SearchRequest(name, SearchType.USERS, page, filters)));
    }

    @GetMapping(params = "type=USERS")
    @Cacheable(value = "results", key = "{#name, page, filters}", cacheManager = "cacheManager")
    public ResponseEntity<List<GitRepoResultResponse>> getReposSearchResult(
            @RequestParam(value = "filters", required = false) List<String> filters,
            @RequestParam String name, @RequestParam Integer page) {
        return ResponseEntity.ok(searchService.getGitRepoSearchResultByClient(new SearchRequest(name, SearchType.REPOSITORIES, page, filters)));
    }
}
