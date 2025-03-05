package com.pqkhang.ct553_backend.domain.booking.order.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface OrderService {
    void createOrderByCustomerId(OrderDTO orderDTO) throws ResourceNotFoundException;

    void updateOrderStatus(String orderId, OrderStatusEnum orderStatusEnum) throws ResourceNotFoundException;

    OrderDTO getOrder(String orderId) throws ResourceNotFoundException;

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getAllOrdersByCustomerId(UUID customerId) throws ResourceNotFoundException;

    Page<OrderDTO> getOrders(Map<String, String> params) throws ResourceNotFoundException;
}