package com.pqkhang.ct553_backend.domain.category.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.category.dto.BuyingPriceDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuyingPriceService {
    List<BuyingPriceDTO> getAllBuyingPricesByProductId(Integer productId) throws ResourceNotFoundException;

    BuyingPriceDTO getCurrentBuyingPriceByProductId(Integer productId) throws ResourceNotFoundException;

    BuyingPriceDTO createBuyingPrice(Integer productId, @Valid BuyingPriceDTO buyingPriceDTO);
}
