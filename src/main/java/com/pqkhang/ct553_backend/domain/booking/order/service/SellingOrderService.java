package com.pqkhang.ct553_backend.domain.booking.order.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface SellingOrderService {
    void createSellingOrder(SellingOrderDTO sellingOrderDTO) throws ResourceNotFoundException;

    void updateSellingOrderStatus(String orderId, OrderStatusEnum orderStatusEnum) throws ResourceNotFoundException;

    SellingOrderDTO getSellingOrder(String orderId) throws ResourceNotFoundException;

    List<SellingOrderDTO> getAllSellingOrders();

    List<SellingOrderDTO> getAllSellingOrdersByCustomerId(UUID customerId) throws ResourceNotFoundException;

    Page<SellingOrderDTO> getSellingOrders(Map<String, String> params) throws ResourceNotFoundException;
}