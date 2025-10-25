package com.zayden_project.addFriend_service.mapper;

import org.mapstruct.Mapper;

import com.zayden_project.addFriend_service.dto.response.FriendResponse;
import com.zayden_project.addFriend_service.entity.Friend;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    FriendResponse toAddFriendResponse(Friend friend);
}
