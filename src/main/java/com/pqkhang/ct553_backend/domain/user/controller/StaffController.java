package com.pqkhang.ct553_backend.domain.user.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.dto.request.ChangePasswordRequest;
import com.pqkhang.ct553_backend.domain.user.dto.StaffDTO;
import com.pqkhang.ct553_backend.domain.user.dto.response.StaffStatisticDTO;
import com.pqkhang.ct553_backend.domain.user.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/staffs")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @GetMapping
    public ApiResponse<Page<StaffDTO>> getStaffs(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<StaffDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(staffService.getStaffs(params))
                .message("Get all staffs successfully")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<StaffDTO>> getAllStaffs() {
        return ApiResponse.<List<StaffDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(staffService.getAllStaffs())
                .message("Get all staffs successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<StaffDTO> getStaffById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        return ApiResponse.<StaffDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(staffService.getStaffById(id))
                .message("Get staff has id " + id + " successfully")
                .build();
    }

    @GetMapping("/email/{email}")
    public ApiResponse<StaffDTO> getStaffByEmail(@PathVariable("email") String email) throws ResourceNotFoundException {
        return ApiResponse.<StaffDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(staffService.getStaffByEmail(email))
                .message("Get staff has email " + email + " successfully")
                .build();
    }

    @GetMapping("/logged-in")
    public ApiResponse<StaffDTO> getLoggedInStaff() {
        return ApiResponse.<StaffDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(staffService.getLoggedInStaff())
                .message("Get logged in staff successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<StaffDTO> createStaff(@Valid @RequestBody StaffDTO staffDTO) throws ResourceNotFoundException {
        return ApiResponse.<StaffDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(staffService.createStaff(staffDTO))
                .message("Tạo nhân viên thành công")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<StaffDTO> updateStaff(@PathVariable("id") UUID id, @RequestBody StaffDTO staffDTO) throws ResourceNotFoundException {
        return ApiResponse.<StaffDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(staffService.updateStaff(id, staffDTO))
                .message("Cập nhật nhân viên thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStaff(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        staffService.deleteStaff(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Xóa nhân viên thành công")
                .build();
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException {
        staffService.changePassword(id, changePasswordRequest);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Đổi mật khẩu thành công")
                .build();
    }

    @GetMapping("/statistic")
    public ApiResponse<List<StaffStatisticDTO>> getStaffStatistic() {
        return ApiResponse.<List<StaffStatisticDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(staffService.getStaffStatistic())
                .message("Get staff statistic successfully")
                .build();
    }
}
