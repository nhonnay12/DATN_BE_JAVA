package com.datn.service;

import com.datn.models.dto.ProductPagingResponse;
import com.datn.models.dto.request.user_role.UserUpdateRequest;
import com.datn.models.dto.response.UserPagingResponse;
import com.datn.models.dto.response.UserResponse;
import com.datn.models.dto.request.user_role.UserCreationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponse createUser(MultipartFile file, UserCreationRequest request);

    UserResponse getMyInfo();

    UserResponse updateUser(MultipartFile file, UserUpdateRequest request);

    void deleteUser(String id);

    List<UserResponse> getAllUsers();

    public UserPagingResponse getAllUserwithPaging(Integer pageNumber, Integer pageSize);

    public UserPagingResponse getAllUserWithPagingAndSort(Integer pageNumber, Integer pageSize, String sortBy, String dir);
}
