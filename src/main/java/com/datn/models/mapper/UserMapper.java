package com.datn.models.mapper;

import com.datn.models.dto.request.user_role.UserUpdateRequest;
import com.datn.models.dto.response.UserResponse;
import com.datn.models.entity.User;
import com.datn.models.dto.request.user_role.UserCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "images", source = "images")
    UserResponse toUserResponse(User user);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
