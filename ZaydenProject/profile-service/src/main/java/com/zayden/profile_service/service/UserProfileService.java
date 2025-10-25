package com.zayden.profile_service.service;

import java.util.List;
import java.util.stream.Collectors;

import com.zayden.profile_service.dto.request.UserIdsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zayden.profile_service.dto.request.ProfileCreationRequest;
import com.zayden.profile_service.dto.request.ProfileUpdateRequest;
import com.zayden.profile_service.dto.request.SearchUserRequest;
import com.zayden.profile_service.dto.response.UserProfileResponse;
import com.zayden.profile_service.entity.UserProfile;
import com.zayden.profile_service.exception.AppException;
import com.zayden.profile_service.exception.ErrorCode;
import com.zayden.profile_service.mapper.UserProfileMapper;
import com.zayden.profile_service.repository.UserProfileRepository;
import com.zayden.profile_service.repository.httpclient.FileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    FileClient fileClient;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse updateProfile(ProfileUpdateRequest request) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userProfileMapper.updateProfile(userProfile, request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public UserProfileResponse updateAvatar(MultipartFile file) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfile.setAvatar(fileClient.uploadMedia(file).getResult().getUrl());

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public List<UserProfileResponse> searchUserProfile(SearchUserRequest request) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserProfile> userProfiles = userProfileRepository.findAllByUsernameLike(request.getUsername());

        return userProfiles.stream()
                .filter(userProfile -> !userId.equals(userProfile.getUserId()))
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    public UserProfileResponse getProfileByUserId(String userId) {
        UserProfile userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfileById(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getMyInfo() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public List<UserProfileResponse> getAllProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    public UserProfileResponse getProfileByUsername(String username) {
        return userProfileMapper.toUserProfileResponse(userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND)));
    }

    public List<UserProfileResponse> getProfileByUserIds(UserIdsRequest request) {
        return userProfileRepository.findByUserIdIn(request.getUserIds()).stream()
                .map(userProfileMapper::toUserProfileResponse)
                .collect(Collectors.toList());
    }

    public boolean existsByUserId(String userId) {
        return userProfileRepository.existsByUserId(userId);
    }
}
