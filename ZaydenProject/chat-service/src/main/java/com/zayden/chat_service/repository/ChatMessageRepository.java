package com.zayden.chat_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zayden.chat_service.entity.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findAllByConversationIdOrderByCreatedDateDesc(String conversationId);
}
