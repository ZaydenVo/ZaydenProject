package com.zayden.chat_service.entity;

import java.time.Instant;

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
@Document(collection = "chat_message")
public class ChatMessage {
    @MongoId
    String id;

    @Indexed
    String conversationId;

    String message;
    ParticipantInfo sender;

    @Indexed
    Instant createdDate;
}
