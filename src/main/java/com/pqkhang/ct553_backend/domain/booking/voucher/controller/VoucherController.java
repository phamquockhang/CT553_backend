package com.pqkhang.ct553_backend.domain.booking.voucher.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.voucher.dto.VoucherDTO;
import com.pqkhang.ct553_backend.domain.booking.voucher.service.VoucherService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vouchers")
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class VoucherController {

    VoucherService voucherService;

    @GetMapping("/{voucherId}")
    public ApiResponse<VoucherDTO> getVoucher(@PathVariable("voucherId") Integer voucherId) throws ResourceNotFoundException {
        return ApiResponse.<VoucherDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(voucherService.getVoucher(voucherId))
                .message("Lấy thông tin voucher thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<VoucherDTO>> getVouchers(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<VoucherDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(voucherService.getVouchers(params))
                .message("Lấy danh sách voucher hợp lệ thành công")
                .build();
    }

    @GetMapping("/valid")
    public ApiResponse<Page<VoucherDTO>> getAllValidVouchers(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<VoucherDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(voucherService.getAllValidVouchers(params))
                .message("Lấy danh sách voucher hợp lệ thành công")
                .build();
    }


    @PostMapping
    public ApiResponse<Void> createVoucher(@Valid @RequestBody VoucherDTO voucherDTO) {
        voucherService.createVoucher(voucherDTO);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Tạo voucher thành công")
                .build();
    }

    @PutMapping("/{voucherId}")
    public ApiResponse<Void> updateVoucher(@PathVariable("voucherId") Integer voucherId, @Valid @RequestBody VoucherDTO voucherDTO) throws ResourceNotFoundException {
        voucherService.updateVoucher(voucherId, voucherDTO);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cập nhật voucher thành công")
                .build();
    }

    @DeleteMapping("/{voucherId}")
    public ApiResponse<Void> deleteVoucher(@PathVariable("voucherId") Integer voucherId) throws ResourceNotFoundException {
        voucherService.deleteVoucher(voucherId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Xóa voucher thành công")
                .build();
    }
}
