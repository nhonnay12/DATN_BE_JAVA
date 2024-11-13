package com.datn.models.dto.request.authen_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyUser {
    String email;
    // String email;
    String verifyCode;
}