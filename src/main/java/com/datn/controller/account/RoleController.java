package com.datn.controller.account;

import com.datn.models.dto.response.ApiResponse;
import com.datn.models.dto.response.RoleResponse;
import com.datn.service.RoleService;
import com.datn.models.dto.request.user_request.RoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleResponse> apiResponse = ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Sucess")
                .result(roleService.createRole(roleRequest))
                .build();
                return apiResponse;
    }
    @PutMapping
    public ApiResponse<RoleResponse> updateRole(@RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Sucess");
        apiResponse.setResult(roleService.updateRole(roleRequest));
        return apiResponse;
    }
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        ApiResponse<List<RoleResponse>> apiResponse = ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .message("Sucess")
                .result(roleService.getAllRole())
                .build();
        return apiResponse;
    }
    @DeleteMapping("/{name}")
    public ApiResponse<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(200)
                .message("Role " + name + " is deleted")
                .build();
        return apiResponse;
    }
    @GetMapping("/{name}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String name) {
        ApiResponse<RoleResponse> apiResponse =  ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Sucess")
                .result(roleService.getRole(name))
                .build();
        return apiResponse;
    }
}
