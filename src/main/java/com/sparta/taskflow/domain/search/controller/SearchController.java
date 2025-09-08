package com.sparta.taskflow.domain.search.controller;

import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.search.dto.response.SearchResponse;
import com.sparta.taskflow.domain.search.service.SearchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchQueryService searchQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<SearchResponse>> unifiedSearch(@RequestParam String query) {
        return ApiResponse.success(
                searchQueryService.unifiedSearch(query),
                "검색 완료"
        );
    }
}
