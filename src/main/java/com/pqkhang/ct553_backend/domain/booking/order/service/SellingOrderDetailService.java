package com.pqkhang.ct553_backend.domain.booking.order.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrderDetail;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellingOrderDetailService {
    List<SellingOrderDetail> createSellingOrderDetail(String sellingOrderId, @Valid List<SellingOrderDetailDTO> sellingOrderDetailDTOList) throws ResourceNotFoundException;

    List<SellingOrderDetailDTO> getAllOrderDetailByOrderId(String orderId) throws ResourceNotFoundException;
}
