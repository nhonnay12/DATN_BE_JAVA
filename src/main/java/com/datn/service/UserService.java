package com.datn.service;

import com.datn.models.dto.request.user_request.UserUpdateRequest;
import com.datn.models.dto.response.UserResponse;
import com.datn.models.dto.request.user_request.UserCreationRequest;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse getMyInfo();
    UserResponse updateUser(UserUpdateRequest request);
    void deleteUser(String id);
    List<UserResponse> getAllUsers();
}
