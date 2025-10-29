package com.zayden.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayden.identity_service.dto.request.RoleRequest;
import com.zayden.identity_service.dto.response.RoleResponse;
import com.zayden.identity_service.mapper.RoleMapper;
import com.zayden.identity_service.repostitory.PermissionRepository;
import com.zayden.identity_service.repostitory.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
