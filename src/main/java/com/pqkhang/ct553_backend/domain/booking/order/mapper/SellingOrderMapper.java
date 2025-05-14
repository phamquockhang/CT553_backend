package com.pqkhang.ct553_backend.domain.booking.order.mapper;

import com.pqkhang.ct553_backend.domain.booking.order.dto.response.OverviewSellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.voucher.mapper.UsedVoucherMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SellingOrderDetailMapper.class, UsedVoucherMapper.class})
public interface SellingOrderMapper {
    @Mapping(source = "customer.customerId", target = "customerId")
    SellingOrderDTO toSellingOrderDTO(SellingOrder sellingOrder);

    @Mapping(source = "customerId", target = "customer.customerId")
    SellingOrder toSellingOrder(SellingOrderDTO sellingOrderDTO);

    OverviewSellingOrderDTO toOverviewSellingOrderDTO(SellingOrder sellingOrder);

    SellingOrderDTO toSellingOrderDTO(OverviewSellingOrderDTO overviewSellingOrderDTO);
}

