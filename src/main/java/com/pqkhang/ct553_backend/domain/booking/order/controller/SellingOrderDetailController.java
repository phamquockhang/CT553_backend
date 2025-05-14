package com.pqkhang.ct553_backend.domain.booking.order.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order_details")
@RequiredArgsConstructor
public class SellingOrderDetailController {

    private final SellingOrderDetailService sellingOrderDetailService;

    @PostMapping("/{orderId}")
    public ApiResponse<List<SellingOrderDetailDTO>> createOrderDetail(@PathVariable("orderId") String orderId, @RequestBody List<SellingOrderDetailDTO> sellingOrderDetailDTOList) throws ResourceNotFoundException {
        sellingOrderDetailService.createSellingOrderDetail(orderId, sellingOrderDetailDTOList);
        return ApiResponse.<List<SellingOrderDetailDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
//                .payload(orderDetailService.createOrderDetail(orderId,orderDetailDTOList))
                .message("Tạo chi tiết đơn hàng thành công")
                .build();
    }
}
