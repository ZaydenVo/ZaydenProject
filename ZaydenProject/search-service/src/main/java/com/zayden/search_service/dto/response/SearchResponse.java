package com.zayden.search_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchResponse {
    List<UserProfileResponse> users;
    PageResponse<PostResponse> posts;
}
