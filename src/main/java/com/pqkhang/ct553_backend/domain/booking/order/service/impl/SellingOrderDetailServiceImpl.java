package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrderDetail;
import com.pqkhang.ct553_backend.domain.booking.order.mapper.SellingOrderDetailMapper;
import com.pqkhang.ct553_backend.domain.booking.order.repository.OrderDetailRepository;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderDetailService;
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
public class SellingOrderDetailServiceImpl implements SellingOrderDetailService {
    SellingOrderRepository sellingOrderRepository;
    SellingOrderDetailMapper sellingOrderDetailMapper;
    OrderDetailRepository orderDetailRepository;

    @Override
    public List<SellingOrderDetail> createSellingOrderDetail(String sellingOrderId, List<SellingOrderDetailDTO> sellingOrderDetailDTOList) throws ResourceNotFoundException {
        sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + sellingOrderId));

        for (SellingOrderDetailDTO sellingOrderDetailDTO : sellingOrderDetailDTOList) {
            SellingOrderDetail newSellingOrderDetail = sellingOrderDetailMapper.toSellingOrderDetail(sellingOrderDetailDTO);
            newSellingOrderDetail.setSellingOrder(SellingOrder.builder().sellingOrderId(sellingOrderId).build());
            newSellingOrderDetail.setProduct(Product.builder().productId(sellingOrderDetailDTO.getProductId()).build());
            orderDetailRepository.save(newSellingOrderDetail);
        }

        return orderDetailRepository.findAllBySellingOrder_SellingOrderId(sellingOrderId);
    }

    @Override
    public List<SellingOrderDetailDTO> getAllOrderDetailByOrderId(String orderId) throws ResourceNotFoundException {
        sellingOrderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));

        List<SellingOrderDetail> sellingOrderDetails = orderDetailRepository.findAllBySellingOrder_SellingOrderId(orderId);
        return sellingOrderDetails.stream().map(sellingOrderDetailMapper::toSellingOrderDetailDTO).collect(Collectors.toList());
    }

}

