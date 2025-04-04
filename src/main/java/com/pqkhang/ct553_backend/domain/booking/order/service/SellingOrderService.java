package com.pqkhang.ct553_backend.domain.booking.order.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.request.RequestSellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.response.SellingOrderStatisticsDTO;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface SellingOrderService {
    SellingOrderDTO createSellingOrder(RequestSellingOrderDTO requestSellingOrderDTO) throws ResourceNotFoundException;

    void updateSellingOrderStatus(String orderId, OrderStatusEnum orderStatusEnum, PaymentStatusEnum paymentStatusEnum) throws ResourceNotFoundException;

    SellingOrderDTO getSellingOrder(String orderId) throws ResourceNotFoundException;

    List<SellingOrderDTO> getAllSellingOrders();

    Page<SellingOrderDTO> getAllSellingOrdersByCustomerId(UUID customerId, Map<String, String> params) throws ResourceNotFoundException;

    Page<SellingOrderDTO> getSellingOrders(Map<String, String> params) throws ResourceNotFoundException;

    SellingOrderStatisticsDTO getSellingOrderStatistics(LocalDateTime startDate, LocalDateTime endDate);
}