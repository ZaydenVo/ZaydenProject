package com.zayden_project.addFriend_service.dto.response;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendResponse {
    String id;
    String user1;
    String user2;
    String senderId;
    String status;
    LocalDateTime createdAt;
}
