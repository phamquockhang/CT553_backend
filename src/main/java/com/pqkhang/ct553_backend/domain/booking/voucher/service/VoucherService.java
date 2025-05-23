package com.pqkhang.ct553_backend.domain.booking.voucher.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.voucher.dto.VoucherDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface VoucherService {
    VoucherDTO getVoucher(Integer id) throws ResourceNotFoundException;

    Page<VoucherDTO> getVouchers(Map<String, String> params) throws ResourceNotFoundException;

    Page<VoucherDTO> getAllValidVouchers(Map<String, String> params) throws ResourceNotFoundException;

    void createVoucher(VoucherDTO voucherDTO);

    void updateVoucher(Integer voucherId, VoucherDTO voucherDTO) throws ResourceNotFoundException;

    void deleteVoucher(Integer id) throws ResourceNotFoundException;

    void useVoucher(String voucherCode) throws ResourceNotFoundException;

    void returnVoucher(String voucherCode) throws ResourceNotFoundException;

    void updateVoucherStatusDaily();
}
