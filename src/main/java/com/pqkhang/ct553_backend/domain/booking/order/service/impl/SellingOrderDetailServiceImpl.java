package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrderDetail;
import com.pqkhang.ct553_backend.domain.booking.order.mapper.SellingOrderDetailMapper;
import com.pqkhang.ct553_backend.domain.booking.order.repository.OrderDetailRepository;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderDetailService;
import com.pqkhang.ct553_backend.domain.category.dto.WeightDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.entity.Weight;
import com.pqkhang.ct553_backend.domain.category.mapper.WeightMapper;
import com.pqkhang.ct553_backend.domain.category.service.WeightService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class SellingOrderDetailServiceImpl implements SellingOrderDetailService {
    SellingOrderRepository sellingOrderRepository;
    SellingOrderDetailMapper sellingOrderDetailMapper;
    OrderDetailRepository orderDetailRepository;
    WeightService weightService;
    WeightMapper weightMapper;

    @Override
    public List<SellingOrderDetail> createSellingOrderDetail(String sellingOrderId, List<SellingOrderDetailDTO> sellingOrderDetailDTOList) throws ResourceNotFoundException {
        sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + sellingOrderId));

//        checkSellingOrderDetails(sellingOrderDetailDTOList);

        for (SellingOrderDetailDTO sellingOrderDetailDTO : sellingOrderDetailDTOList) {
            SellingOrderDetail newSellingOrderDetail = sellingOrderDetailMapper.toSellingOrderDetail(sellingOrderDetailDTO);
            newSellingOrderDetail.setSellingOrder(SellingOrder.builder().sellingOrderId(sellingOrderId).build());
            newSellingOrderDetail.setProduct(Product.builder().productId(sellingOrderDetailDTO.getProductId()).build());

            if (sellingOrderDetailDTO.getUnit().contains("kg")) {
                newSellingOrderDetail.setTotalWeight(sellingOrderDetailDTO.getQuantity());
            }

            orderDetailRepository.save(newSellingOrderDetail);

//            if (newSellingOrderDetail.getTotalWeight() != null) {
//                WeightDTO oldWeight = weightService.getCurrentWeightByProductId(newSellingOrderDetail.getProduct().getProductId());
//                BigDecimal newWeightValue = BigDecimal.valueOf(
//                        oldWeight.getWeightValue().doubleValue() - newSellingOrderDetail.getTotalWeight()
//                );
//
//                Weight newWeight = Weight.builder().weightValue(newWeightValue).build();
//                weightService.createWeight(newSellingOrderDetail.getProduct().getProductId(), weightMapper.toWeightDTO(newWeight));
//            }
        }

        return orderDetailRepository.findAllBySellingOrder_SellingOrderId(sellingOrderId);
    }

    @Override
    public void updateWeightInSellingOrderDetail(List<SellingOrderDetailDTO> sellingOrderDetailDTOList) throws ResourceNotFoundException {
        checkSellingOrderDetails(sellingOrderDetailDTOList);

        for (SellingOrderDetailDTO sellingOrderDetailDTO : sellingOrderDetailDTOList) {
            SellingOrderDetail sellingOrderDetail = orderDetailRepository.findById(sellingOrderDetailDTO.getSellingOrderDetailId()).orElseThrow();
            WeightDTO oldWeight = weightService.getCurrentWeightByProductId(sellingOrderDetail.getProduct().getProductId());
            BigDecimal newWeightValue = BigDecimal.valueOf(
                    oldWeight.getWeightValue().doubleValue() - sellingOrderDetailDTO.getTotalWeight()
            );

            Weight newWeight = Weight.builder().weightValue(newWeightValue).build();
            weightService.createWeight(sellingOrderDetail.getProduct().getProductId(), weightMapper.toWeightDTO(newWeight));

            if (sellingOrderDetail.getTotalWeight() == null) {
                sellingOrderDetail.setTotalWeight(sellingOrderDetailDTO.getTotalWeight());
                orderDetailRepository.save(sellingOrderDetail);
            }
        }
    }

    private void checkSellingOrderDetails(List<SellingOrderDetailDTO> sellingOrderDetailDTOList) throws ResourceNotFoundException {
        for (SellingOrderDetailDTO sellingOrderDetailDTO : sellingOrderDetailDTOList) {
            SellingOrderDetail sellingOrderDetail = orderDetailRepository.findById(sellingOrderDetailDTO.getSellingOrderDetailId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi tiết đơn hàng với id: " + sellingOrderDetailDTO.getSellingOrderDetailId()));
            WeightDTO oldWeight = weightService.getCurrentWeightByProductId(sellingOrderDetail.getProduct().getProductId());
            BigDecimal newWeightValue = BigDecimal.valueOf(
                    oldWeight.getWeightValue().doubleValue() - sellingOrderDetailDTO.getTotalWeight()
            );
            if (newWeightValue.compareTo(BigDecimal.ZERO) < 0) {
                throw new ResourceNotFoundException("Trọng lượng còn lại trong kho của sản phẩm " + sellingOrderDetail.getProduct().getProductName() + " không đủ!!");
            }
        }
    }

    @Override
    public List<SellingOrderDetailDTO> getAllOrderDetailByOrderId(String orderId) throws ResourceNotFoundException {
        sellingOrderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));

        List<SellingOrderDetail> sellingOrderDetails = orderDetailRepository.findAllBySellingOrder_SellingOrderId(orderId);
        return sellingOrderDetails.stream().map(sellingOrderDetailMapper::toSellingOrderDetailDTO).collect(Collectors.toList());
    }

}

