package com.pqkhang.ct553_backend.domain.booking.order.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface OrderService {
    OrderDTO createOrderByCustomerId(OrderDTO orderDTO) throws ResourceNotFoundException;

    OrderDTO updateOrder(OrderDTO orderDTO) throws ResourceNotFoundException;

    OrderDTO getOrder(String orderId) throws ResourceNotFoundException;

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getAllOrdersByCustomerId(UUID customerId) throws ResourceNotFoundException;

    Page<OrderDTO> getOrders(Map<String, String> params) throws ResourceNotFoundException;
}
