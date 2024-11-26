package com.datn.controller.account;

import com.datn.constant.PageParam;
import com.datn.dto.request.user_role.UserCreationRequest;
import com.datn.dto.request.user_role.UserUpdateRequest;
import com.datn.dto.response.ApiResponse;
import com.datn.dto.response.UserPagingResponse;
import com.datn.dto.response.UserResponse;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
//    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest userCreationRequest){
    public ApiResponse<UserResponse> createUser(
            @Valid @RequestParam("username") @Size(min = 4, message = "USERNAME_INVALID") String username,
            @Valid @RequestParam("password") @Size(min = 8, message = "INVALID_PASSWORD") String password,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) @NotEmpty(message = "EMAIL_NOT_NULL")
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "EMAIL_INVALID_GMAIL_FORMAT") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "roles", required = false) String roles,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "userImage", required = false) MultipartFile file
    ) {

        UserCreationRequest userCreationRequest = UserCreationRequest
                .builder()
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .status(status)
                .roles(roles)
                .build();

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Thêm người dùng thành công!!!")
                .result(userService.createUser(file, userCreationRequest))
                .build();
        return apiResponse;
    }

    //    @CachePut(cacheNames = "cache2", key = "'user_' + #request.id")

    @PutMapping
    public ApiResponse<UserResponse> updateUser(
            @RequestParam(value = "id", required = true) String id,  // Đảm bảo id được truyền
            @Valid @RequestParam(value = "username", required = false) @Size(min = 4, message = "USERNAME_INVALID") String username,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "userImage", required = false) MultipartFile file,
            @RequestParam(value = "phone", required = false) String phone
    ) {
        if (id == null || id.trim().isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED); // Tạo lỗi nếu ID bị thiếu
        }

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .id(id)
                .roles(role)
                .status(status)
                .phone(phone)
                .username(username)
                .build();
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Success")
                .result(userService.updateUser(file, userUpdateRequest))
                .build();
        return apiResponse;
    }

    //    @CacheEvict(cacheNames = "cache1", allEntries = true) // Xóa tất cả các entry trong cache1
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

    //    @Cacheable(cacheNames = "cache1", key = "'user'")
    @GetMapping("/getall")
    public ApiResponse<List<UserResponse>> getAllUser() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Success")
                .result(userService.getAllUsers())
                .build();
    }
    @GetMapping("/getUserPaging")
    public ApiResponse<UserPagingResponse> getAllUserWithPaging(
            @RequestParam(defaultValue = PageParam.PAGE_NUM) Integer pageNumber,
            @RequestParam(defaultValue = PageParam.PAGE_SIZE) Integer pageSize
    ) {
        return ApiResponse.<UserPagingResponse>builder()
                .result(userService.getAllUserwithPaging(pageNumber, pageSize))
                .code(200)
                .message("sucess")
                .build();

    }

    @GetMapping("/getProductPagingSort")
    public ApiResponse<UserPagingResponse> getAllUserWithPagingSort(
            @RequestParam(defaultValue = PageParam.PAGE_NUM) Integer pageNumber,
            @RequestParam(defaultValue = PageParam.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = PageParam.SORT_BY) String sortBy,
            @RequestParam(defaultValue = PageParam.SORT_DIR) String sortDir
    ) {
        return ApiResponse.<UserPagingResponse>builder()
                .result(userService.getAllUserWithPagingAndSort(pageNumber, pageSize, sortBy, sortDir))
                .code(200)
                .message("sucess")
                .build();

    }
}
