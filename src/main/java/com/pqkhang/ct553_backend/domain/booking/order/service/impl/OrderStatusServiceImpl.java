package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderStatus;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
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
    SellingOrderRepository sellingOrderRepository;
    OrderStatusRepository orderStatusRepository;

    @Override
    public List<OrderStatus> createOrderStatus(String sellingOrderId, OrderStatusEnum orderStatus) throws ResourceNotFoundException {
        sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + sellingOrderId));

//        OrderStatus newOrderStatus = OrderStatus.builder().status(orderStatus).build();
        OrderStatus newOrderStatus = new OrderStatus();
        newOrderStatus.setStatus(orderStatus);
        newOrderStatus.setSellingOrder(SellingOrder.builder().sellingOrderId(sellingOrderId).build());
        orderStatusRepository.save(newOrderStatus);

        return orderStatusRepository.findAllBySellingOrder_SellingOrderId(sellingOrderId);
    }
}

