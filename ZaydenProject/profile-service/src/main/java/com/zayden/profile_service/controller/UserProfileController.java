package com.zayden.profile_service.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.zayden.profile_service.dto.ApiResponse;
import com.zayden.profile_service.dto.request.ProfileUpdateRequest;
import com.zayden.profile_service.dto.request.SearchUserRequest;
import com.zayden.profile_service.dto.response.UserProfileResponse;
import com.zayden.profile_service.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @GetMapping("/users/{profileId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String profileId) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfileById(profileId))
                .build();
    }

    @GetMapping("/users/myInfo")
    ApiResponse<UserProfileResponse> getMyInfo(){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getMyInfo())
                .build();
    }

    @PutMapping("users/my-profile")
    ApiResponse<UserProfileResponse> updateUser(@RequestBody ProfileUpdateRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateProfile(request))
                .build();
    }

    @PutMapping("/users/avatar")
    ApiResponse<UserProfileResponse> updateAvatar(@RequestParam("file") MultipartFile file) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateAvatar(file))
                .build();
    }

    @GetMapping("/users/profile/{username}")
    ApiResponse<UserProfileResponse> getProfileByUsername(@PathVariable("username") String username){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfileByUsername(username))
                .build();
    }
}
