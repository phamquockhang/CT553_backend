package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.entity.Order;
import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderStatus;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.repository.OrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.repository.OrderStatusRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderStatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class OrderStatusServiceImpl implements OrderStatusService {
    OrderRepository orderRepository;
    OrderStatusRepository orderStatusRepository;

    @Override
    public List<OrderStatus> createOrderStatus(String orderId, OrderStatusEnum orderStatus) throws ResourceNotFoundException {
        orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));

//        OrderStatus newOrderStatus = OrderStatus.builder().status(orderStatus).build();
        OrderStatus newOrderStatus = new OrderStatus();
        newOrderStatus.setStatus(orderStatus);
        newOrderStatus.setOrder(Order.builder().orderId(orderId).build());
        orderStatusRepository.save(newOrderStatus);

        return orderStatusRepository.findAllByOrder_OrderId(orderId);
    }
}

