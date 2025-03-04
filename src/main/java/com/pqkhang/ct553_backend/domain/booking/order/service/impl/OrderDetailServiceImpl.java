package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.Order;
import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderDetail;
import com.pqkhang.ct553_backend.domain.booking.order.mapper.OrderDetailMapper;
import com.pqkhang.ct553_backend.domain.booking.order.repository.OrderDetailRepository;
import com.pqkhang.ct553_backend.domain.booking.order.repository.OrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderDetailService;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {
    OrderRepository orderRepository;
    OrderDetailMapper orderDetailMapper;
    OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> createOrderDetail(String orderId, List<OrderDetailDTO> orderDetailDTOList) throws ResourceNotFoundException {
        orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));

        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            OrderDetail newOrderDetail = orderDetailMapper.toOrderDetail(orderDetailDTO);
            newOrderDetail.setOrder(Order.builder().orderId(orderId).build());
            newOrderDetail.setProduct(Product.builder().productId(orderDetailDTO.getProductId()).build());
            orderDetailRepository.save(newOrderDetail);
        }

        return orderDetailRepository.findAllByOrder_OrderId(orderId);
    }

    @Override
    public List<OrderDetailDTO> getAllOrderDetailByOrderId(String orderId) throws ResourceNotFoundException {
        orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));

        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder_OrderId(orderId);
        return orderDetails.stream().map(orderDetailMapper::toOrderDetailDTO).collect(Collectors.toList());
    }

}

