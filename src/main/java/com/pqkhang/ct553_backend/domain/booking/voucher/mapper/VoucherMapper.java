package com.pqkhang.ct553_backend.domain.booking.voucher.mapper;

import com.pqkhang.ct553_backend.domain.booking.voucher.dto.VoucherDTO;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.Voucher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsedVoucherMapper.class})
public interface VoucherMapper {
    VoucherDTO toVoucherDTO(Voucher voucher);

    Voucher toVoucher(VoucherDTO voucherDTO);
}
