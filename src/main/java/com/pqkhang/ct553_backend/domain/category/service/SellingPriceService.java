package com.pqkhang.ct553_backend.domain.category.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.category.dto.SellingPriceDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellingPriceService {
    List<SellingPriceDTO> getAllSellingPricesByProductId(Integer productId) throws ResourceNotFoundException;

    SellingPriceDTO getCurrentSellingPriceByProductId(Integer productId) throws ResourceNotFoundException;

    SellingPriceDTO createSellingPrice(Integer productId, @Valid SellingPriceDTO sellingPriceDTO);
}
