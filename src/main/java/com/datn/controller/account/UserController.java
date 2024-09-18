package com.datn.controller.account;

import com.datn.models.dto.request.user_request.UserUpdateRequest;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.dto.response.UserResponse;
import com.datn.models.dto.request.user_request.UserCreationRequest;
import com.datn.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Success")
                .result(userService.createUser(request))
                .build();
        return apiResponse;
    }

    @CachePut(cacheNames = "cache2", key = "'user_' + #request.id")
    @PutMapping
    public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Success")
                .result(userService.updateUser(request))
                .build();
        return apiResponse;
    }


    @CacheEvict(cacheNames = "cache1", allEntries = true) // Xóa tất cả các entry trong cache1
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .code(200)
                .message("Success")
                .result("User has been delete")
                .build();
        return apiResponse;
    }

    @GetMapping()
    public ApiResponse<UserResponse> getUser() {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Success")
                .result(userService.getMyInfo())
                .build();
        return apiResponse;
    }

    @Cacheable(cacheNames = "cache1", key = "'user'")
    @GetMapping("/getall")
    public ApiResponse<List<UserResponse>> getAllUser() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Success")
                .result(userService.getAllUsers())
                .build();
    }

}
