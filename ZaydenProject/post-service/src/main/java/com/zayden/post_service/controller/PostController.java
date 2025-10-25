package com.zayden.post_service.controller;

import com.zayden.post_service.dto.request.SearchPostRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.zayden.post_service.dto.ApiResponse;
import com.zayden.post_service.dto.request.PostRequest;
import com.zayden.post_service.dto.response.PageResponse;
import com.zayden.post_service.dto.response.PostResponse;
import com.zayden.post_service.service.PostService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostController {
    PostService postService;

    @PostMapping("/create")
    ApiResponse<PostResponse> createPost(@ModelAttribute PostRequest request,
                                         @RequestParam(value = "file", required = false)MultipartFile file) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request, file))
                .build();
    }

    @GetMapping("/my-posts")
    ApiResponse<PageResponse<PostResponse>> myPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getMyPosts(page, size))
                .build();
    }

    @GetMapping("/followingPosts")
    ApiResponse<PageResponse<PostResponse>> followingPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getFollowingPosts(page, size))
                .build();
    }

    @PostMapping("/search")
    ApiResponse<PageResponse<PostResponse>> search(@RequestBody SearchPostRequest request) {
        return  ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.searchPost(request))
                .build();
    }

    @GetMapping("/lastest")
    ApiResponse<List<PostResponse>> getPostLastest() {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getPostLastest())
                .build();
    }

    @GetMapping("/lastestBefore")
    ApiResponse<List<PostResponse>> getPostLastestBefore(@RequestParam String beforeId) {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getPostLastestBefore(beforeId))
                .build();
    }

    @DeleteMapping("/{postId}")
    ApiResponse<String> deletePost(@PathVariable("postId") String postId) {
        return ApiResponse.<String>builder()
                .result(postService.deletePost(postId))
                .build();
    }
}
