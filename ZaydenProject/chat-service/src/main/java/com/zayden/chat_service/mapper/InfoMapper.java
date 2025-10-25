package com.zayden.chat_service.mapper;

import org.mapstruct.Mapper;

import com.zayden.chat_service.dto.response.UserProfileResponse;
import com.zayden.chat_service.entity.ParticipantInfo;

@Mapper(componentModel = "spring")
public interface InfoMapper {
    ParticipantInfo toParticipantInfo(UserProfileResponse userProfileResponse);
}
