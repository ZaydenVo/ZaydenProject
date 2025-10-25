package com.zayden.search_service.controller;

import com.zayden.search_service.dto.ApiResponse;
import com.zayden.search_service.dto.request.SearchRequest;
import com.zayden.search_service.dto.response.SearchResponse;
import com.zayden.search_service.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {
    SearchService searchService;

    @PostMapping("/search")
    ApiResponse<SearchResponse> search(@RequestBody SearchRequest request) {
        return ApiResponse.<SearchResponse>builder()
                .result(searchService.search(request))
                .build();
    }
}
