package com.datn.mapper;

import com.datn.dto.request.user_role.PermissionRequest;
import com.datn.dto.response.PermissionResponse;
import com.datn.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
    void updatePermission(@MappingTarget Permission permission, PermissionRequest request);
}
