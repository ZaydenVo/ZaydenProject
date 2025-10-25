package com.zayden.chat_service.dto.response;

import java.time.Instant;
import java.util.List;

import com.zayden.chat_service.entity.ParticipantInfo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;
    String type;
    String participantsHash;
    String conversationAvatar;
    String conversationName;
    List<ParticipantInfo> participants;
    Instant createdDate;
    Instant modifiedDate;
}
