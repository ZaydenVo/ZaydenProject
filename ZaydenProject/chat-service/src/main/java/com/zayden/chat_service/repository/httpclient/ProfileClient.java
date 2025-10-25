package com.zayden.chat_service.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zayden.chat_service.configuration.AuthenticationRequestInterceptor;
import com.zayden.chat_service.dto.ApiResponse;
import com.zayden.chat_service.dto.response.UserProfileResponse;

@FeignClient(
        name = "profile-service",
        url = "${app.services.profile.url}",
        configuration = AuthenticationRequestInterceptor.class)
public interface ProfileClient {
    @GetMapping(value = "/internal/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId);
}
