package com.pqkhang.ct553_backend.domain.booking.cart.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDetailInfoDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.service.CartDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart_details")
@RequiredArgsConstructor
public class CartDetailController {

    private final CartDetailService cartDetailService;

    @PostMapping
    public ApiResponse<List<CartDetailDTO>> createCartDetail(@RequestBody CartDetailInfoDTO cartDetailInfoDTO) throws ResourceNotFoundException {
        return ApiResponse.<List<CartDetailDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(cartDetailService.createCartDetail(cartDetailInfoDTO))
                .message("Tạo chi tiết giỏ hàng thành công")
                .build();
    }


    @GetMapping("/{cartId}")
    public ApiResponse<List<CartDetailDTO>> getCartDetailByCartId(@PathVariable("cartId") Integer cartId) throws ResourceNotFoundException {
        return ApiResponse.<List<CartDetailDTO>>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(cartDetailService.getCartDetailByCartId(cartId))
                .message("Lấy thông tin chi tiết giỏ hàng thành công")
                .build();
    }

}
