package com.pqkhang.ct553_backend.domain.booking.order.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderAndOrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderDTO> createOrderByCustomerId(@RequestBody OrderAndOrderDetailDTO orderAndOrderDetailDTO) throws ResourceNotFoundException {
        return ApiResponse.<OrderDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(orderService.createOrderByCustomerId(orderAndOrderDetailDTO))
                .message("Tạo đơn hàng thành công")
                .build();
    }

    @PutMapping
    public ApiResponse<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO) throws ResourceNotFoundException {
        return ApiResponse.<OrderDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(orderService.updateOrder(orderDTO))
                .message("Cập nhật đơn hàng thành công")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<OrderDTO>> getAllOrders() {
        return ApiResponse.<List<OrderDTO>>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(orderService.getAllOrders())
                .message("Lấy thông tin tất cả đơn hàng thành công")
                .build();
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<OrderDTO>> getAllOrdersByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<List<OrderDTO>>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(orderService.getAllOrdersByCustomerId(customerId))
                .message("Lấy thông tin tất cả đơn hàng của khách hàng thành công")
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDTO> getOrder(@PathVariable("orderId") String orderId) throws ResourceNotFoundException {
        return ApiResponse.<OrderDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(orderService.getOrder(orderId))
                .message("Lấy thông tin đơn hàng thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<OrderDTO>> getOrders(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<OrderDTO>>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(orderService.getOrders(params))
                .message("Lấy thông tin tất cả đơn hàng thành công")
                .build();
    }
}
