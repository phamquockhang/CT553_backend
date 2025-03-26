package com.pqkhang.ct553_backend.domain.booking.order.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.request.RequestSellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.response.SellingOrderStatisticsDTO;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/selling_orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellingOrderController {

    SellingOrderService sellingOrderService;
    SellingOrderRepository sellingOrderRepository;

    @PostMapping
    public ApiResponse<SellingOrderDTO> createSellingOrder(@Valid @RequestBody RequestSellingOrderDTO requestSellingOrderDTO) throws ResourceNotFoundException {
//        sellingOrderService.createSellingOrder(sellingOrderDTO);
        return ApiResponse.<SellingOrderDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(sellingOrderService.createSellingOrder(requestSellingOrderDTO))
                .message("Tạo đơn hàng thành công")
                .build();
    }

    @PutMapping("/{sellingOrderId}")
    public ApiResponse<Void> updateSellingOrderStatus(@PathVariable("sellingOrderId") String sellingOrderId, @RequestBody SellingOrderDTO sellingOrderDTO) throws ResourceNotFoundException {
        // transform orderStatus and paymentStatus from String to Enum
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(sellingOrderDTO.getOrderStatus());
        PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum.valueOf(sellingOrderDTO.getPaymentStatus());

        sellingOrderService.updateSellingOrderStatus(sellingOrderId, orderStatusEnum, paymentStatusEnum);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cập nhật trạng thái đơn hàng thành công")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<SellingOrderDTO>> getAllSellingOrders() {
        return ApiResponse.<List<SellingOrderDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(sellingOrderService.getAllSellingOrders())
                .message("Lấy thông tin tất cả đơn hàng thành công")
                .build();
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<SellingOrderDTO>> getAllSellingOrdersByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<List<SellingOrderDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(sellingOrderService.getAllSellingOrdersByCustomerId(customerId))
                .message("Lấy thông tin tất cả đơn hàng của khách hàng thành công")
                .build();
    }

    @GetMapping("/{sellingOrderId}")
    public ApiResponse<SellingOrderDTO> getSellingOrder(@PathVariable("sellingOrderId") String sellingOrderId) throws ResourceNotFoundException {
        return ApiResponse.<SellingOrderDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(sellingOrderService.getSellingOrder(sellingOrderId))
                .message("Lấy thông tin đơn hàng thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<SellingOrderDTO>> getSellingOrders(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<SellingOrderDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(sellingOrderService.getSellingOrders(params))
                .message("Lấy thông tin tất cả đơn hàng thành công")
                .build();
    }

    @GetMapping("/selling-order-statistics")
    public ApiResponse<SellingOrderStatisticsDTO> getSellingOrderStatistics(@RequestParam String period) {
        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        switch (period.toLowerCase()) {
            case "today" -> startDate = LocalDate.now().atStartOfDay();
            case "yesterday" -> {
                startDate = LocalDate.now().minusDays(1).atStartOfDay();
                endDate = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);
            }
            case "this-week" -> startDate = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
            case "last-week" -> {
                startDate = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1).atStartOfDay();
                endDate = startDate.plusDays(6).toLocalDate().atTime(LocalTime.MAX);
            }
            case "this-month" -> startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            case "last-month" -> {
                startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
                endDate = startDate.plusMonths(1).minusDays(1).toLocalDate().atTime(LocalTime.MAX);
            }
            case "this-year" -> startDate = LocalDate.now().withDayOfYear(1).atStartOfDay();
            case "last-year" -> {
                startDate = LocalDate.now().minusYears(1).withDayOfYear(1).atStartOfDay();
                endDate = startDate.plusYears(1).minusDays(1).toLocalDate().atTime(LocalTime.MAX);
            }
            case "all-time" -> {
                startDate = sellingOrderRepository.findOldestOrderDate().orElse(LocalDate.of(1970, 1, 1)).atStartOfDay();
                endDate = LocalDateTime.now();
            }

            default ->
                    throw new IllegalArgumentException("Giai đoạn " + period + " không hợp lệ, chỉ hỗ trợ today, yesterday, this-week, last-week, this-month, last-month, this-year, last-year, all-time");
        }

        return ApiResponse.<SellingOrderStatisticsDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(sellingOrderService.getSellingOrderStatistics(startDate, endDate))
                .message("Lấy thông tin thống kê đơn hàng thành công")
                .build();
    }
}
