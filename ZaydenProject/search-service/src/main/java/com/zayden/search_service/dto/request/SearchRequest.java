package com.zayden.search_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchRequest {
    int page;
    int size;
    String text;

    @Builder.Default
    String type = "all";
}
