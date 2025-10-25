package com.zayden_project.addFriend_service.controller;

import java.util.List;
import java.util.Map;

import com.zayden_project.addFriend_service.enums.FriendStatus;
import org.springframework.web.bind.annotation.*;

import com.zayden_project.addFriend_service.dto.ApiResponse;
import com.zayden_project.addFriend_service.dto.request.AcceptFriendRequest;
import com.zayden_project.addFriend_service.dto.request.AddFriendRequest;
import com.zayden_project.addFriend_service.dto.response.FriendResponse;
import com.zayden_project.addFriend_service.dto.response.UserProfileResponse;
import com.zayden_project.addFriend_service.service.FriendService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendController {
    FriendService friendService;

    @PostMapping("/addFriend")
    public ApiResponse<FriendResponse> addFriend(@RequestBody AddFriendRequest request) {
        return ApiResponse.<FriendResponse>builder()
                .result(friendService.addFriend(request))
                .build();
    }

    @PostMapping("/acceptFriend")
    public ApiResponse<FriendResponse> acceptFriend(@RequestBody AcceptFriendRequest request) {
        return ApiResponse.<FriendResponse>builder()
                .result(friendService.acceptFriend(request))
                .build();
    }

    @DeleteMapping("/cancel/{friendId}")
    public ApiResponse<String> cancelFriendRequest(@PathVariable("friendId") String friendId) {
        return ApiResponse.<String>builder()
                .result(friendService.cancelFriendRequest(friendId))
                .build();
    }

    @DeleteMapping("/reject/{friendId}")
    public ApiResponse<String> rejectFriend(@PathVariable("friendId") String friendId) {
        return ApiResponse.<String>builder()
                .result(friendService.rejectFriendRequest(friendId))
                .build();
    }

    @DeleteMapping("/unfriend/{friendId}")
    public ApiResponse<String> unFriend(@PathVariable("friendId") String friendId) {
        return ApiResponse.<String>builder()
                .result(friendService.unfriend(friendId))
                .build();
    }

    @GetMapping("/suggested")
    public ApiResponse<List<UserProfileResponse>> getSuggestedUsers() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(friendService.getSuggestedUsers())
                .build();
    }

    @GetMapping("/following")
    public ApiResponse<List<UserProfileResponse>> getFollowingUsers() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(friendService.getFollowingUsers())
                .build();
    }

    @GetMapping("/status/{friendId}")
    public ApiResponse<Map<String, Object>> getFriendStatus(@PathVariable("friendId") String friendId){
        return ApiResponse.<Map<String, Object>>builder()
                .result(friendService.getFriendStatus(friendId))
                .build();
    }

    @GetMapping("/followingUserIds")
    public ApiResponse<List<String>> getFollowingUserIds() {
        return ApiResponse.<List<String>>builder()
                .result(friendService.getFollowingUserIds())
                .build();
    }
}
