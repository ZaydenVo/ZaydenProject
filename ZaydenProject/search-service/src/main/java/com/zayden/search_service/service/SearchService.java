package com.zayden.search_service.service;

import com.zayden.search_service.dto.request.SearchPostRequest;
import com.zayden.search_service.dto.request.SearchRequest;
import com.zayden.search_service.dto.request.SearchUserRequest;
import com.zayden.search_service.dto.response.PageResponse;
import com.zayden.search_service.dto.response.PostResponse;
import com.zayden.search_service.dto.response.SearchResponse;
import com.zayden.search_service.dto.response.UserProfileResponse;
import com.zayden.search_service.repository.httpClient.postClient.PostClient;
import com.zayden.search_service.repository.httpClient.profileClient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SearchService {
    ProfileClient profileClient;
    PostClient postClient;

    public SearchResponse search(SearchRequest request) {
        List<UserProfileResponse> users = new ArrayList<>();
        PageResponse<PostResponse> posts = new PageResponse<>();
        if (request.getType().equals("all") || request.getType().equals("users")) {
            users = profileClient.searchUsers(SearchUserRequest.builder().username(request.getText()).build()).getResult();
        }

        if (request.getType().equals("all") || request.getType().equals("posts")) {
            posts = postClient.searchPost(SearchPostRequest.builder()
                        .page(request.getPage() <=0 ? 1 : request.getPage())
                        .size(request.getSize() <=0 ? 10 : request.getSize())
                        .text(request.getText())
                        .build())
                .getResult();
        }

        return SearchResponse.builder()
                .users(users)
                .posts(posts)
                .build();
    }
}
