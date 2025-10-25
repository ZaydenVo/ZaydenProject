package com.zayden.chat_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zayden.chat_service.dto.ApiResponse;
import com.zayden.chat_service.dto.request.ChatMessageRequest;
import com.zayden.chat_service.dto.response.ChatMessageResponse;
import com.zayden.chat_service.service.ChatMessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("message")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageController {
    ChatMessageService chatMessageService;

    @PostMapping("/create")
    ApiResponse<ChatMessageResponse> create(@RequestBody @Valid ChatMessageRequest request)
            throws JsonProcessingException {
        return ApiResponse.<ChatMessageResponse>builder()
                .result(chatMessageService.createMessage(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ChatMessageResponse>> getMessage(@RequestParam("conversationId") String conversationId) {
        return ApiResponse.<List<ChatMessageResponse>>builder()
                .result(chatMessageService.getMessage(conversationId))
                .build();
    }
}
