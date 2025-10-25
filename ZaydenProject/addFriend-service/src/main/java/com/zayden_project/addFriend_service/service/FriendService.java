package com.zayden_project.addFriend_service.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zayden_project.addFriend_service.dto.request.AcceptFriendRequest;
import com.zayden_project.addFriend_service.dto.request.AddFriendRequest;
import com.zayden_project.addFriend_service.dto.request.UserIdsRequest;
import com.zayden_project.addFriend_service.dto.response.FriendResponse;
import com.zayden_project.addFriend_service.dto.response.UserProfileResponse;
import com.zayden_project.addFriend_service.entity.Friend;
import com.zayden_project.addFriend_service.enums.FriendStatus;
import com.zayden_project.addFriend_service.exception.AppException;
import com.zayden_project.addFriend_service.exception.ErrorCode;
import com.zayden_project.addFriend_service.mapper.FriendMapper;
import com.zayden_project.addFriend_service.repository.FriendRepository;
import com.zayden_project.addFriend_service.repository.httpClient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendService {
    FriendRepository friendRepository;
    FriendMapper friendMapper;
    ProfileClient profileClient;

    public FriendResponse addFriend(AddFriendRequest request) {
        if (!profileClient.existsByUserId(request.getFriendId()).getResult())
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        var currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUserId.equals(request.getFriendId())) throw new AppException(ErrorCode.CANNOT_ADD_SELF);

        if (friendRepository
                .findBetweenUsers(currentUserId, request.getFriendId())
                .isPresent()) throw new AppException(ErrorCode.ALREADY_SENT_REQUEST);

        var friend = Friend.builder()
                .user1(currentUserId)
                .user2(request.getFriendId())
                .senderId(currentUserId)
                .status(FriendStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        friendRepository.save(friend);
        return friendMapper.toAddFriendResponse(friend);
    }

    public FriendResponse acceptFriend(AcceptFriendRequest request) {
        var currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();

        var friend = friendRepository
                .findBetweenUsers(currentUserId, request.getSenderId())
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friend.getStatus().equals(FriendStatus.PENDING)) throw new AppException(ErrorCode.INVALID_REQUEST);

        if (friend.getSenderId().equals(currentUserId)) throw new AppException(ErrorCode.CANNOT_ACCEPT_SELF_REQUEST);

        friend.setStatus(FriendStatus.FRIEND);
        friend.setUpdatedAt(LocalDateTime.now());
        friendRepository.save(friend);

        return friendMapper.toAddFriendResponse(friend);
    }

    public String cancelFriendRequest(String friendId) {
        var currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();

        var friend = friendRepository
                .findBetweenUsers(currentUserId, friendId)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friend.getSenderId().equals(currentUserId)) throw new AppException(ErrorCode.PERMISSION_DENIED);

        if (!friend.getStatus().equals(FriendStatus.PENDING)) throw new AppException(ErrorCode.INVALID_REQUEST);

        friendRepository.delete(friend);
        return "Friend Request has been cancel!";
    }

    public String rejectFriendRequest(String senderId) {
        var currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();

        var friend = friendRepository
                .findBetweenUsers(currentUserId, senderId)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (friend.getSenderId().equals(currentUserId)) throw new AppException(ErrorCode.CANNOT_REJECT_OWN_REQUEST);

        if (!friend.getStatus().equals(FriendStatus.PENDING)) throw new AppException(ErrorCode.INVALID_REQUEST);

        friendRepository.delete(friend);
        return "Friend Request has been rejected!";
    }

    public String unfriend(String friendId) {
        var currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();

        var friend = friendRepository
                .findBetweenUsers(currentUserId, friendId)
                .orElseThrow(() -> new AppException(ErrorCode.FRIENDSHIPS_NOT_FOUND));

        if (!friend.getStatus().equals(FriendStatus.FRIEND)) throw new AppException(ErrorCode.INVALID_REQUEST);

        friendRepository.delete(friend);
        return "Unfriend successfully!";
    }

    public List<UserProfileResponse> getSuggestedUsers() {
        var currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();

        var relatedIds = friendRepository.findAllRelatedUserIds(currentUserId);
        relatedIds.add(currentUserId);

        var allProfiles = profileClient.getAllUserProfiles().getResult();

        var suggested = allProfiles.stream()
                .filter(p -> !relatedIds.contains(p.getUserId()))
                .collect(Collectors.toList());

        Collections.shuffle(suggested);
        return suggested.stream().limit(4).collect(Collectors.toList());
    }

    public List<UserProfileResponse> getFollowingUsers() {
        var currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();

        List<FriendStatus> statuses = List.of(FriendStatus.FRIEND, FriendStatus.PENDING);
        var followingIds = friendRepository.findFollowingIdsBySender(currentUserId, statuses);
        if (followingIds.isEmpty()) return Collections.emptyList();

        var followingUsers = profileClient
                .getProfilesByIds(UserIdsRequest.builder().userIds(followingIds).build())
                .getResult();

        Collections.shuffle(followingUsers);
        return followingUsers.stream().limit(4).collect(Collectors.toList());
    }

    public List<String> getFollowingUserIds() {
        var currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<FriendStatus> statuses = List.of(FriendStatus.FRIEND, FriendStatus.PENDING);
        return friendRepository.findFollowingIdsBySender(currentUserId, statuses);
    }

    public Map<String, Object> getFriendStatus(String friendId) {
        var currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        var friend = friendRepository.findBetweenUsers(currentUserId, friendId).orElse(null);
        if (friend == null) return Map.of("status", FriendStatus.NONE);
        return Map.of(
                "status", friend.getStatus(),
                "senderId", friend.getSenderId()
        );
    }
}
