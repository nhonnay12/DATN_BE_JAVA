package com.datn.service.impl;

import com.datn.entity.Permission;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.mapper.PermissionMapper;
import com.datn.dto.request.user_role.PermissionRequest;
import com.datn.dto.response.PermissionResponse;
import com.datn.repository.PermissionRepository;
import com.datn.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        var existingPermission = permissionRepository.findById(request.getName());
        if(existingPermission.isPresent()){
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = permissionMapper.toPermission(request);

        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public List<PermissionResponse> getAllPermission() {
        return permissionRepository.findAll().stream().map(permissionMapper :: toPermissionResponse).toList();
    }

    @Override
    public PermissionResponse updatePermission(PermissionRequest request) {
        Permission permission = permissionRepository.findById(request.getName()).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permissionMapper.updatePermission(permission, request);

        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public void deletePermission(String name) {
        permissionRepository.deleteById(name);
    }
}
