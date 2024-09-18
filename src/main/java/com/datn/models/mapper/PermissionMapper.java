package com.datn.models.mapper;

import com.datn.models.dto.request.user_request.PermissionRequest;
import com.datn.models.dto.response.PermissionResponse;
import com.datn.models.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
    void updatePermission(@MappingTarget Permission permission, PermissionRequest request);
}
