package com.pqkhang.ct553_backend.domain.booking.order.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderStatusDTO;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/selling_orders")
@RequiredArgsConstructor
public class SellingOrderController {

    private final SellingOrderService sellingOrderService;

    @PostMapping
    public ApiResponse<Void> createSellingOrder(@Valid @RequestBody SellingOrderDTO sellingOrderDTO) throws ResourceNotFoundException {
        sellingOrderService.createSellingOrder(sellingOrderDTO);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Tạo đơn hàng thành công")
                .build();
    }

    @PutMapping("/{sellingOrderId}")
    public ApiResponse<Void> updateSellingOrderStatus(@PathVariable("sellingOrderId") String sellingOrderId, @RequestBody OrderStatusDTO orderStatusDTO) throws ResourceNotFoundException {
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(orderStatusDTO.getStatus()); // transform orderStatus from String to OrderStatusEnum

        sellingOrderService.updateSellingOrderStatus(sellingOrderId, orderStatusEnum);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cập nhật trạng thái đơn hàng thành công")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<SellingOrderDTO>> getAllSellingOrders() {
        return ApiResponse.<List<SellingOrderDTO>>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(sellingOrderService.getAllSellingOrders())
                .message("Lấy thông tin tất cả đơn hàng thành công")
                .build();
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<SellingOrderDTO>> getAllSellingOrdersByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<List<SellingOrderDTO>>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(sellingOrderService.getAllSellingOrdersByCustomerId(customerId))
                .message("Lấy thông tin tất cả đơn hàng của khách hàng thành công")
                .build();
    }

    @GetMapping("/{sellingOrderId}")
    public ApiResponse<SellingOrderDTO> getSellingOrder(@PathVariable("sellingOrderId") String sellingOrderId) throws ResourceNotFoundException {
        return ApiResponse.<SellingOrderDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(sellingOrderService.getSellingOrder(sellingOrderId))
                .message("Lấy thông tin đơn hàng thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<SellingOrderDTO>> getSellingOrders(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<SellingOrderDTO>>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(sellingOrderService.getSellingOrders(params))
                .message("Lấy thông tin tất cả đơn hàng thành công")
                .build();
    }
}
