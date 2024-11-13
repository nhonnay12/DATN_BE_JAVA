package com.datn.service;

import com.datn.models.dto.request.user_role.RoleRequest;
import com.datn.models.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse getRole(String name);
    RoleResponse createRole(RoleRequest request);
    List<RoleResponse> getAllRole();
    RoleResponse updateRole(RoleRequest request);
    void deleteRole(String name);
}
