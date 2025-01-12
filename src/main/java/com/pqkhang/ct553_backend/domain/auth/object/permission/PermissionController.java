package com.pqkhang.ct553_backend.domain.auth.object.permission;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
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
    ){
        return ApiResponse.<Page<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Permissions fetched successfully")
                .payload(permissionService.getPermissions(params))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<PermissionDTO>> getAllPermissions(){
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
                .message("Permission created successfully")
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
    public ApiResponse<PermissionDTO> updatePermission(@PathVariable("id") Long id ,@Valid @RequestBody PermissionDTO permissionDTO) throws ResourceNotFoundException {
        return ApiResponse.<PermissionDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Permission updated successfully")
                .payload(permissionService.updatePermission(id, permissionDTO))
                .build();
    }
}
