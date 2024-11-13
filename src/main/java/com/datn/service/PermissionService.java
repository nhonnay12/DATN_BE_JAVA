package com.datn.service;

import com.datn.models.dto.request.user_role.PermissionRequest;
import com.datn.models.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    List<PermissionResponse> getAllPermission();
    PermissionResponse updatePermission(PermissionRequest request);
    void deletePermission(String name);
}
