package com.zayden.chat_service.dto.response;

import java.time.Instant;

import com.zayden.chat_service.entity.ParticipantInfo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    String id;
    String conversationId;
    boolean me;
    String message;
    ParticipantInfo sender;
    Instant createdDate;
}
