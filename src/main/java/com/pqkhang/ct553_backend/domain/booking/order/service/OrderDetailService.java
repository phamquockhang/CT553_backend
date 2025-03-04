package com.pqkhang.ct553_backend.domain.booking.order.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderDetail;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderDetailService {
    List<OrderDetail> createOrderDetail(String orderId, @Valid List<OrderDetailDTO> orderDetailDTOList) throws ResourceNotFoundException;

    List<OrderDetailDTO> getAllOrderDetailByOrderId(String orderId) throws ResourceNotFoundException;
}
