package com.zayden.chat_service.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zayden.chat_service.dto.request.ConversationRequest;
import com.zayden.chat_service.dto.response.ConversationResponse;
import com.zayden.chat_service.entity.Conversation;
import com.zayden.chat_service.entity.ParticipantInfo;
import com.zayden.chat_service.exception.AppException;
import com.zayden.chat_service.exception.ErrorCode;
import com.zayden.chat_service.mapper.ConversationMapper;
import com.zayden.chat_service.mapper.InfoMapper;
import com.zayden.chat_service.repository.ConversationRepository;
import com.zayden.chat_service.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ProfileClient profileClient;
    ConversationRepository conversationRepository;
    InfoMapper infoMapper;
    ConversationMapper conversationMapper;

    public ConversationResponse createConversation(ConversationRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userInfo = profileClient.getProfile(userId).getResult();
        var participantInfo =
                profileClient.getProfile(request.getParticipantIds().getFirst()).getResult();

        if (Objects.isNull(userInfo) || Objects.isNull(participantInfo))
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);

        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(participantInfo.getUserId());
        var sortedIds = userIds.stream().sorted().toList();

        String userIdsHash = generateParticipantsHash(sortedIds);

        var conversation = conversationRepository
                .findByParticipantsHash(userIdsHash)
                .orElseGet(() -> {
                    List<ParticipantInfo> participantInfos = List.of(
                            infoMapper.toParticipantInfo(userInfo), infoMapper.toParticipantInfo(participantInfo));
                    Conversation newConversation = Conversation.builder()
                            .type(request.getType())
                            .participantsHash(userIdsHash)
                            .createdDate(Instant.now())
                            .modifiedDate(Instant.now())
                            .participants(participantInfos)
                            .conversationAvatar(participantInfo.getAvatar())
                            .conversationName(participantInfo.getUsername())
                            .build();
                    return conversationRepository.save(newConversation);
                });

        return toConversationResponse(conversation);
    }

    public List<ConversationResponse> myConversations() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Conversation> conversations = conversationRepository.findAllByParticipantIdsContains(userId);

        return conversations.stream().map(this::toConversationResponse).toList();
    }

    public String deleteConversation(String conversationId) {
        var conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
        conversationRepository.delete(conversation);
        return "Conversation has been deleted!";
    }

    private String generateParticipantsHash(List<String> ids) {
        StringJoiner stringJoiner = new StringJoiner("_");
        ids.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);

        conversation.getParticipants().stream()
                .filter(participantInfo -> !participantInfo.getUserId().equals(userId))
                .findFirst()
                .ifPresent(participantInfo -> {
                    conversationResponse.setConversationName(participantInfo.getUsername());
                    conversationResponse.setConversationAvatar(participantInfo.getAvatar());
                });
        return conversationResponse;
    }
}
