package com.datn.service.impl;

import com.datn.models.dto.response.RoleResponse;
import com.datn.models.entity.Role;
import com.datn.models.exception.AppException;
import com.datn.models.mapper.RoleMapper;
import com.datn.repository.PermissionRepository;
import com.datn.repository.RoleRepository;
import com.datn.service.RoleService;
import com.datn.models.dto.request.user_role.RoleRequest;
import com.datn.models.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public RoleResponse getRole(String name) {
        var role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));

        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRole() {

        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public RoleResponse updateRole(RoleRequest request) {
        Role role = roleRepository.findById(request.getName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roleMapper.updateRole(role, request);

        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(String name) {
        roleRepository.deleteById(name);
    }
}
