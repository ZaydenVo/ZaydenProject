package com.zayden.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.zayden.identity_service.dto.ApiResponse;
import com.zayden.identity_service.dto.request.PermissionRequest;
import com.zayden.identity_service.dto.response.PermissionResponse;
import com.zayden.identity_service.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    String delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return "Permission " + permission + " has been deleted!";
    }
}
