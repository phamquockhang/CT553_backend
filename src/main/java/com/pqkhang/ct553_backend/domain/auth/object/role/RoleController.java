package com.pqkhang.ct553_backend.domain.auth.object.role;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ApiResponse<Page<RoleDTO>> getRoles(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<RoleDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Get all roles successfully")
                .payload(roleService.getRoles(params))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<RoleDTO>> getAllRoles() {
        return ApiResponse.<List<RoleDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Get all roles successfully")
                .payload(roleService.getAllRoles())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDTO> getRoleById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Get role has id " + id + " successfully")
                .payload(roleService.getRoleById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) throws ResourceNotFoundException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Tạo vai trò thành công")
                .payload(roleService.createRole(roleDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable("id") Long id) throws ResourceNotFoundException {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Xóa vai trò thành công")
                .build();
    }

    @PutMapping({"/{id}"})
    public ApiResponse<RoleDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) throws ResourceNotFoundException {
        return ApiResponse.<RoleDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cập nhật vai trò thành công")
                .payload(roleService.updateRole(id, roleDTO))
                .build();
    }

}
