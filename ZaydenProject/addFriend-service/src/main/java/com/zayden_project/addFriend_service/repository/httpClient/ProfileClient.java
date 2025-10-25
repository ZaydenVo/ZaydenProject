package com.zayden_project.addFriend_service.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.zayden_project.addFriend_service.configuration.AuthenticationRequestInterceptor;
import com.zayden_project.addFriend_service.dto.ApiResponse;
import com.zayden_project.addFriend_service.dto.request.UserIdsRequest;
import com.zayden_project.addFriend_service.dto.response.UserProfileResponse;

@FeignClient(
        name = "profile-service",
        url = "${app.services.profile}",
        configuration = AuthenticationRequestInterceptor.class)
public interface ProfileClient {
    @GetMapping(value = "/internal/exists/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> existsByUserId(@PathVariable("userId") String userId);

    @GetMapping("/internal/users")
    ApiResponse<List<UserProfileResponse>> getAllUserProfiles();

    @PostMapping(value = "/internal/batch", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<UserProfileResponse>> getProfilesByIds(@RequestBody UserIdsRequest request);
}
