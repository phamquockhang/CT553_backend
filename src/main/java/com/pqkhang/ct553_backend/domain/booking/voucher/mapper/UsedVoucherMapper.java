package com.pqkhang.ct553_backend.domain.booking.voucher.mapper;

import com.pqkhang.ct553_backend.domain.booking.voucher.dto.UsedVoucherDTO;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.UsedVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsedVoucherMapper {
    @Mapping(target = "voucherCode", source = "voucher.voucherCode")
    @Mapping(target = "sellingOrderId", source = "sellingOrder.sellingOrderId")
    UsedVoucherDTO toUsedVoucherDTO(UsedVoucher usedVoucher);

    @Mapping(target = "voucher.voucherCode", source = "voucherCode")
    @Mapping(target = "sellingOrder.sellingOrderId", source = "sellingOrderId")
    UsedVoucher toUsedVoucher(UsedVoucherDTO usedVoucherDTO);
}
