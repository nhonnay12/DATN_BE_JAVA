package com.datn.mapper;

import com.datn.dto.request.user_role.RoleRequest;
import com.datn.dto.response.RoleResponse;
import com.datn.entity.Permission;
import com.datn.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class RoleMapper {


    public Role toRole(RoleRequest request) {
        if ( request == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.name( request.getName() );
        role.description( request.getDescription() );

        return role.build();
    }


    public RoleResponse toRoleResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.name( role.getName() );
        roleResponse.description( role.getDescription() );
        Set<Permission> set = role.getPermissions();
        if ( set != null ) {
            roleResponse.permissions( new LinkedHashSet<Permission>( set ) );
        }

        return roleResponse.build();
    }


    public void updateRole(Role role, RoleRequest request) {
        if ( request == null ) {
            return;
        }

        role.setName( request.getName() );
        role.setDescription( request.getDescription() );
    }
}
