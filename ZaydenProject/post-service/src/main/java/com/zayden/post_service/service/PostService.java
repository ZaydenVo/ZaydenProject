package com.zayden.post_service.service;

import java.time.Instant;
import java.util.List;

import com.zayden.post_service.dto.request.SearchPostRequest;
import com.zayden.post_service.repository.httpclient.FileClient;
import com.zayden.post_service.repository.httpclient.FriendClient;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zayden.post_service.dto.request.PostRequest;
import com.zayden.post_service.dto.response.PageResponse;
import com.zayden.post_service.dto.response.PostResponse;
import com.zayden.post_service.dto.response.UserProfileResponse;
import com.zayden.post_service.entity.Post;
import com.zayden.post_service.mapper.PostMapper;
import com.zayden.post_service.repository.PostRepository;
import com.zayden.post_service.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    ProfileClient profileClient;
    FileClient fileClient;
    DateTimeFormatter dateTimeFormatter;
    FriendClient friendClient;

    public PostResponse createPost(PostRequest request, MultipartFile file) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String fileUrl = null;
        if(file != null) {
            fileUrl = fileClient.uploadFile(file).getResult().getUrl();
        }

        Post post = Post.builder()
                .userId(userId)
                .title(request.getTitle())
                .file(fileUrl)
                .content(request.getContent())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();

        postRepository.save(post);

        UserProfileResponse userProfile = null;
        try {
            userProfile = profileClient.getUserProfile(userId).getResult();
        } catch (Exception e) {
            log.error("Error while getting user profile", e);
        }

        var username = userProfile != null ? userProfile.getUsername() : null;

        PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setUsername(username);
        postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));

        return postResponse;
    }

    public PageResponse<PostResponse> getMyPosts(int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileResponse userProfile = null;
        try {
            userProfile = profileClient.getUserProfile(userId).getResult();
        } catch (Exception e) {
            log.error("Error while getting user profile", e);
        }

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = postRepository.findAllByUserId(userId, pageable);

        String username = userProfile != null ? userProfile.getUsername() : null;

        var postList = pageData.stream()
                .map(post -> {
                    var postResponse = postMapper.toPostResponse(post);
                    postResponse.setUsername(username);
                    postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
                    return postResponse;
                })
                .toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pagesSize(pageData.getSize())
                .totalPage(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
    }

    public PageResponse<PostResponse> getFollowingPosts(int page, int size) {
        var followingUserIds = friendClient.getFollowingUserIds().getResult();
        Pageable pageable = PageRequest.of(page -1, size);

        var pageData = postRepository.findByUserIdInOrderByCreatedDateDesc(followingUserIds, pageable);

        var postList = pageData.stream().map(this::toPostResponse).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pagesSize(pageData.getSize())
                .totalPage(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
    }

    public PageResponse<PostResponse> searchPost(SearchPostRequest request) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        var pageData = postRepository.searchText(request.getText(), pageable);

        var postList = pageData.stream().map(this::toPostResponse).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(request.getPage())
                .pagesSize(pageData.getSize())
                .totalPage(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
    }

    public List<PostResponse> getPostLastest() {
        return postRepository.findTop5ByOrderByIdDesc()
                .stream()
                .map(post -> {
                    var postResponse = postMapper.toPostResponse(post);
                    try {
                        var userProfile = profileClient.getUserProfile(post.getUserId()).getResult();
                        postResponse.setUsername(userProfile.getUsername());
                    } catch (Exception e) {
                        log.warn("Failed to fetch username for userId={}", post.getUserId(), e);
                        postResponse.setUsername("Unknown");
                    }

                    postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
                    return postResponse;
                })
                .toList();
    }

    public List<PostResponse> getPostLastestBefore(String beforeId) {
        return postRepository.findTop5ByIdLessThanOrderByIdDesc(beforeId)
                .stream()
                .map(post -> {
                    var postResponse = postMapper.toPostResponse(post);
                    try {
                        var userProfile = profileClient.getUserProfile(post.getUserId()).getResult();
                        postResponse.setUsername(userProfile.getUsername());
                    } catch (Exception e) {
                        log.warn("Failed to fetch username for userId={}", post.getUserId(), e);
                        postResponse.setUsername("Unknown");
                    }

                    postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
                    return postResponse;
                })
                .toList();
    }

    public String deletePost(String postId) {
        postRepository.deleteById(postId);
        return "Post has been deleted!";
    }

    private PostResponse toPostResponse(Post post) {
        var postResponse = postMapper.toPostResponse(post);
        try {
            var userProfile = profileClient.getUserProfile(post.getUserId()).getResult();
            postResponse.setUsername(userProfile.getUsername());
        } catch (Exception e) {
            log.warn("Failed to fetch username for userId={}", post.getUserId(), e);
            postResponse.setUsername("Unknown");
        }

        postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
        return postResponse;
    }
}
