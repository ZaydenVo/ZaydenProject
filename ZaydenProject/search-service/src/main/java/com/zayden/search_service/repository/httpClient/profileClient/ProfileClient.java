package com.zayden.search_service.repository.httpClient.profileClient;

import com.zayden.search_service.dto.ApiResponse;
import com.zayden.search_service.dto.request.SearchUserRequest;
import com.zayden.search_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @PostMapping("/internal/users/search")
    ApiResponse<List<UserProfileResponse>> searchUsers(@RequestBody SearchUserRequest request);
}
