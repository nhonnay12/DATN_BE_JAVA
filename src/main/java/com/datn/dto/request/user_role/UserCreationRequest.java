package com.datn.dto.request.user_role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    private String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String firstName;
    private String lastName;
    @NotEmpty(message = "EMAIL_NOT_NULL")  //@NotNull(message = "EMAIL_NOT_NULL") vẫn giữ nguyên để kiểm tra xem email có bị null không.
    private String email;
    //@NotEmpty(message = "PHONE_NOT_NULL") thay thế cho @NotNull để kiểm tra chuỗi phone không chỉ null mà còn không rỗng.
    //@NotEmpty(message = "PHONE_NOT_NULL")
    private String phone;
    private String roles;
    private String status= "ACTIVE";
}
