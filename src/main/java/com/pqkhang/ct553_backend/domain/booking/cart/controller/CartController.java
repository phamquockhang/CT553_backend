package com.pqkhang.ct553_backend.domain.booking.cart.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{customerId}")
    public ApiResponse<CartDTO> createCartByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<CartDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(cartService.createCartByCustomerId(customerId))
                .message("Tạo giỏ hàng thành công")
                .build();
    }


    @GetMapping("/{customerId}")
    public ApiResponse<CartDTO> getCartByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<CartDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(cartService.getCartByCustomerId(customerId))
                .message("Lấy thông tin giỏ hàng thành công")
                .build();
    }

}
