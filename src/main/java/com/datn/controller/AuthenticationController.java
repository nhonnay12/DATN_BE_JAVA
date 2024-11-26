package com.datn.controller;
import com.datn.dto.request.authen_request.*;
import com.datn.dto.request.user_role.UserCreationRequest;
import com.datn.dto.response.ApiResponse;
import com.datn.dto.response.AuthenticationResponse;
import com.datn.dto.response.IntrospectResponse;
import com.datn.entity.User;
import com.datn.service.impl.AuthenticationServiceImpl;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationServiceImpl authenticationService;

    @PostMapping("/signup")
    ApiResponse<User> signup(@Valid @RequestBody UserCreationRequest request) {
        var result = authenticationService.signup(request);
        return ApiResponse.<User>builder()
                .code(200)
                .result(result).build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .result(result).build();
    }

    @PostMapping("/verify_user")
    ApiResponse<IntrospectResponse> verify(@RequestBody VerifyUser request)
            throws ParseException, JOSEException {
        authenticationService.verifyUser(request);
        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .message("sucess")
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectResponseApiResponse(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .result(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .build();
    }
}
