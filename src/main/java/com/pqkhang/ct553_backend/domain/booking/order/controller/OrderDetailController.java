package com.pqkhang.ct553_backend.domain.booking.order.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping("/{orderId}")
    public ApiResponse<List<OrderDetailDTO>> createOrderDetail(@PathVariable("orderId") String orderId, @RequestBody List<OrderDetailDTO> orderDetailDTOList) throws ResourceNotFoundException {
        orderDetailService.createOrderDetail(orderId, orderDetailDTOList);
        return ApiResponse.<List<OrderDetailDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
//                .payload(orderDetailService.createOrderDetail(orderId,orderDetailDTOList))
                .message("Tạo chi tiết đơn hàng thành công")
                .build();
    }
}
