package com.datn.models.dto.request.user_role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {
    private String id;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String roles;
    private String username;
    private String status;
}
