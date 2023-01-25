package com.dragonguard.backend.search.controller;

import com.dragonguard.backend.Result.dto.response.ResultResponse;
import com.dragonguard.backend.search.entity.GitRepoSortType;
import com.dragonguard.backend.search.entity.SortDirection;
import com.dragonguard.backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping(value = "/git-repos", params = "type=repository")
    public ResponseEntity<List<ResultResponse>> getGitRepos(
            @RequestParam(name = "q") String q,
            @RequestParam(name = "sortBy") GitRepoSortType sortBy,
            @RequestParam(name = "sort") SortDirection sort,
            Pageable pageable) {
        return ResponseEntity.ok(searchService.getAllRepositoryName(q, sortBy, sort, pageable));
    }
}
