package com.pqkhang.ct553_backend.domain.auth.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.dto.PermissionDTO;
import com.pqkhang.ct553_backend.domain.auth.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<Page<PermissionDTO>> getPermissions(
            @RequestParam Map<String, String> params
    ) throws ResourceNotFoundException {
        return ApiResponse.<Page<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Permissions fetched successfully")
                .payload(permissionService.getPermissions(params))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<PermissionDTO>> getAllPermissions() {
        return ApiResponse.<List<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Permissions fetched successfully")
                .payload(permissionService.getAllPermissions())
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) throws ResourceNotFoundException {
        return ApiResponse.<PermissionDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Tạo quyền mới thành công")
                .payload(permissionService.createPermission(permissionDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable("id") Long id) throws ResourceNotFoundException {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Permission deleted successfully")
                .build();
    }

    @PutMapping({"/{id}"})
    public ApiResponse<PermissionDTO> updatePermission(@PathVariable("id") Long id, @Valid @RequestBody PermissionDTO permissionDTO) throws ResourceNotFoundException {
        return ApiResponse.<PermissionDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cập nhật quyền thành công")
                .payload(permissionService.updatePermission(id, permissionDTO))
                .build();
    }
}
