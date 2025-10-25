package com.zayden.chat_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.zayden.chat_service.dto.ApiResponse;
import com.zayden.chat_service.dto.request.ConversationRequest;
import com.zayden.chat_service.dto.response.ConversationResponse;
import com.zayden.chat_service.service.ConversationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("conversations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/create")
    ApiResponse<ConversationResponse> createConversation(@RequestBody @Valid ConversationRequest request) {
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.createConversation(request))
                .build();
    }

    @GetMapping("/my-conversation")
    ApiResponse<List<ConversationResponse>> myConversations() {
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationService.myConversations())
                .build();
    }

    @DeleteMapping("/delete/{conversationId}")
    ApiResponse<String> deleteConversation(@PathVariable("conversationId") String conversationId) {
        return ApiResponse.<String>builder()
                .result(conversationService.deleteConversation(conversationId))
                .build();
    }
}
