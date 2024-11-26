package com.datn.controller.account;

import com.datn.dto.request.user_role.PermissionRequest;
import com.datn.dto.response.ApiResponse;
import com.datn.dto.response.PermissionResponse;
import com.datn.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        ApiResponse<PermissionResponse> permissionResponseApiResponse = ApiResponse.<PermissionResponse>builder()
                .code(200)
                .message("Sucess")
                .result(permissionService.createPermission(request))
                .build();
        return  permissionResponseApiResponse;
    }
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        ApiResponse<List<PermissionResponse>> perApiResponse = ApiResponse.<List<PermissionResponse>>builder()
                .code(200)
                .message("Sucess")
                .result(permissionService.getAllPermission())
                .build();
        return perApiResponse;
    }
    @DeleteMapping("{name}")
    public ApiResponse<Void> deletePermission(@PathVariable String name) {
        permissionService.deletePermission(name);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(200)
                .message(" " + name + " is deleted")
                .build();
        return apiResponse;
    }

    @PutMapping
    public ApiResponse<PermissionResponse> updatePermission( @RequestBody PermissionRequest request) {
        ApiResponse<PermissionResponse> apiResponse = ApiResponse.<PermissionResponse>builder()
                .code(200)
                .message("Sucess")
                .result(permissionService.updatePermission(request))
                .build();
        return apiResponse;
    }
}
