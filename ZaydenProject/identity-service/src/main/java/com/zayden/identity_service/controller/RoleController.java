package com.zayden.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.zayden.identity_service.dto.ApiResponse;
import com.zayden.identity_service.dto.request.RoleRequest;
import com.zayden.identity_service.dto.response.RoleResponse;
import com.zayden.identity_service.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getRoles())
                .build();
    }

    @DeleteMapping("/{role}")
    String delete(@PathVariable String role) {
        roleService.deleteRole(role);
        return "Role " + role + " has been deleted!";
    }
}
