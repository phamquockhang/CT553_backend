package com.pqkhang.ct553_backend.domain.transaction.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.transaction.dto.PaymentMethodDTO;
import com.pqkhang.ct553_backend.domain.transaction.service.PaymentMethodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMethodController {

    PaymentMethodService paymentMethodService;

    @GetMapping
    public ApiResponse<Page<PaymentMethodDTO>> getPaymentMethods(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<PaymentMethodDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(paymentMethodService.getPaymentMethods(params))
                .message("Lấy danh sách phương thức thanh toán thành công")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<PaymentMethodDTO>> getAllPaymentMethods() {
        return ApiResponse.<List<PaymentMethodDTO>>builder()
                .status(200)
                .success(true)
                .payload(paymentMethodService.getAllPaymentMethods())
                .message("Lấy danh sách phương thức thanh toán thành công")
                .build();
    }

    @PostMapping
    public ApiResponse<PaymentMethodDTO> createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        return ApiResponse.<PaymentMethodDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(paymentMethodService.createPaymentMethod(paymentMethodDTO))
                .message("Tạo phương thức thanh toán thành công")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PaymentMethodDTO> updatePaymentMethod(@PathVariable Integer id, @RequestBody PaymentMethodDTO paymentMethodDTO) throws ResourceNotFoundException {
        return ApiResponse.<PaymentMethodDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(paymentMethodService.updatePaymentMethod(id, paymentMethodDTO))
                .message("Cập nhật phương thức thanh toán thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePaymentMethod(@PathVariable Integer id) throws ResourceNotFoundException {
        paymentMethodService.deletePaymentMethod(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Xóa phương thức thanh toán thành công")
                .build();
    }
}
