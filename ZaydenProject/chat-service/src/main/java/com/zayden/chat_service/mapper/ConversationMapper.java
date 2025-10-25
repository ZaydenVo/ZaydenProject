package com.zayden.chat_service.mapper;

import org.mapstruct.Mapper;

import com.zayden.chat_service.dto.response.ConversationResponse;
import com.zayden.chat_service.entity.Conversation;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toConversationResponse(Conversation conversation);
}
