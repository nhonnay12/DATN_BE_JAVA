package com.datn.service;

import com.datn.dto.request.user_role.RoleRequest;
import com.datn.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse getRole(String name);
    RoleResponse createRole(RoleRequest request);
    List<RoleResponse> getAllRole();
    RoleResponse updateRole(RoleRequest request);
    void deleteRole(String name);
}
