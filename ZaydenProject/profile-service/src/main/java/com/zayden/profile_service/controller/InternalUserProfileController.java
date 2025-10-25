package com.zayden.profile_service.controller;

import com.zayden.profile_service.dto.request.SearchUserRequest;
import com.zayden.profile_service.dto.request.UserIdsRequest;
import org.springframework.web.bind.annotation.*;

import com.zayden.profile_service.dto.ApiResponse;
import com.zayden.profile_service.dto.request.ProfileCreationRequest;
import com.zayden.profile_service.dto.response.UserProfileResponse;
import com.zayden.profile_service.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/users")
    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createProfile(request))
                .build();
    }

    @GetMapping("users/{userId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfileByUserId(userId))
                .build();
    }

    @PostMapping("users/search")
    ApiResponse<List<UserProfileResponse>> searchUser(@RequestBody SearchUserRequest request) {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.searchUserProfile(request))
                .build();
    }

    @GetMapping("/users")
    ApiResponse<List<UserProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllProfiles())
                .build();
    }

    @GetMapping("/exists/{userId}")
    ApiResponse<Boolean> existsByUserId(@PathVariable("userId") String userId) {
        return ApiResponse.<Boolean>builder()
                .result(userProfileService.existsByUserId(userId))
                .build();
    }

    @PostMapping("/batch")
    ApiResponse<List<UserProfileResponse>> getProfilesByIds(@RequestBody UserIdsRequest request) {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getProfileByUserIds(request))
                .build();
    }
}
