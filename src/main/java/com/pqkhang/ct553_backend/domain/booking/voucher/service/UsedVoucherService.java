package com.pqkhang.ct553_backend.domain.booking.voucher.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.voucher.dto.UsedVoucherDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UsedVoucherService {
    UsedVoucherDTO getUsedVoucher(Integer usedVoucherId) throws ResourceNotFoundException;

    Page<UsedVoucherDTO> getUsedVouchers(Map<String, String> params) throws ResourceNotFoundException;

    void createUsedVoucher(UsedVoucherDTO usedVoucherDTO);

    void deleteVoucher(Integer usedVoucherId) throws ResourceNotFoundException;
}
