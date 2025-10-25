package com.zayden.search_service.repository.httpClient.postClient;

import com.zayden.search_service.dto.ApiResponse;
import com.zayden.search_service.dto.request.SearchPostRequest;
import com.zayden.search_service.dto.response.PageResponse;
import com.zayden.search_service.dto.response.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "post-service", url = "${app.services.post.url}")
public interface PostClient {
    @PostMapping("/search")
    ApiResponse<PageResponse<PostResponse>> searchPost(@RequestBody SearchPostRequest request);
}
