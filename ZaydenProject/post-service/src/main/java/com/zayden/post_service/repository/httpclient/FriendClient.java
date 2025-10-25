package com.zayden.post_service.repository.httpclient;

import com.zayden.post_service.configuration.AuthenticationRequestInterceptor;
import com.zayden.post_service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "friend-service", url = "${app.services.friend.url}", configuration = AuthenticationRequestInterceptor.class)
public interface FriendClient {
    @GetMapping("/followingUserIds")
    ApiResponse<List<String>> getFollowingUserIds();
}
