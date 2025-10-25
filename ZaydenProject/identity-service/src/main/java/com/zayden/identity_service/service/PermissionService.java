package com.zayden.identity_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zayden.identity_service.dto.request.PermissionRequest;
import com.zayden.identity_service.dto.response.PermissionResponse;
import com.zayden.identity_service.entity.Permission;
import com.zayden.identity_service.mapper.PermissionMapper;
import com.zayden.identity_service.repostitory.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
