package com.zayden.chat_service.mapper;

import org.mapstruct.Mapper;

import com.zayden.chat_service.dto.request.ChatMessageRequest;
import com.zayden.chat_service.dto.response.ChatMessageResponse;
import com.zayden.chat_service.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);

    ChatMessage toChatMessage(ChatMessageRequest request);
}
