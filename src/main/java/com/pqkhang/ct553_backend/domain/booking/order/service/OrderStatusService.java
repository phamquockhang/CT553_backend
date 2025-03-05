package com.pqkhang.ct553_backend.domain.booking.order.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderStatus;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderStatusService {
    List<OrderStatus> createOrderStatus(String orderId, OrderStatusEnum orderStatus) throws ResourceNotFoundException;
}
