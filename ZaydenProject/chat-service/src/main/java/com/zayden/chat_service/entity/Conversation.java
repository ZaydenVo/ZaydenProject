package com.zayden.chat_service.entity;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "conversation")
public class Conversation {
    @MongoId
    String id;

    String type;

    @Indexed(unique = true)
    String participantsHash;

    List<ParticipantInfo> participants;
    Instant createdDate;
    Instant modifiedDate;
    String conversationName;
    String conversationAvatar;
}
