package com.datn.mapper;

import com.datn.dto.request.user_role.PermissionRequest;
import com.datn.dto.response.PermissionResponse;
import com.datn.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {


    public Permission toPermission(PermissionRequest request) {
        if ( request == null ) {
            return null;
        }

        Permission.PermissionBuilder permission = Permission.builder();

        permission.name( request.getName() );
        permission.description( request.getDescription() );

        return permission.build();
    }


    public PermissionResponse toPermissionResponse(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionResponse.PermissionResponseBuilder permissionResponse = PermissionResponse.builder();

        permissionResponse.name( permission.getName() );
        permissionResponse.description( permission.getDescription() );

        return permissionResponse.build();
    }


    public void updatePermission(Permission permission, PermissionRequest request) {
        if ( request == null ) {
            return;
        }

        permission.setName( request.getName() );
        permission.setDescription( request.getDescription() );
    }
}