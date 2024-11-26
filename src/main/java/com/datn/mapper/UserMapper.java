package com.datn.mapper;

import com.datn.dto.request.user_role.UserUpdateRequest;
import com.datn.dto.response.UserResponse;
import com.datn.entity.User;
import com.datn.dto.request.user_role.UserCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "images", source = "images")
    @Mapping(target = "status", source = "status")
    UserResponse toUserResponse(User user);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
