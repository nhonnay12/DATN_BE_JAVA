package com.datn.models.mapper;

import com.datn.models.dto.request.user_request.UserUpdateRequest;
import com.datn.models.dto.response.UserResponse;
import com.datn.models.entity.User;
import com.datn.models.dto.request.user_request.UserCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
