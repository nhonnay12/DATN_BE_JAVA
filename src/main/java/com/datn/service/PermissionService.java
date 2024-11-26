package com.datn.service;

import com.datn.dto.request.user_role.PermissionRequest;
import com.datn.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    List<PermissionResponse> getAllPermission();
    PermissionResponse updatePermission(PermissionRequest request);
    void deletePermission(String name);
}
