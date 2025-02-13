package com.pqkhang.ct553_backend.domain.category.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.category.dto.BuyingPriceDTO;
import com.pqkhang.ct553_backend.domain.category.entity.BuyingPrice;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.mapper.BuyingPriceMapper;
import com.pqkhang.ct553_backend.domain.category.repository.BuyingPriceRepository;
import com.pqkhang.ct553_backend.domain.category.service.BuyingPriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class BuyingPriceServiceImpl implements BuyingPriceService {

    BuyingPriceRepository buyingPriceRepository;
    BuyingPriceMapper buyingPriceMapper;

    @Override
    public List<BuyingPriceDTO> getAllBuyingPricesByProductId(Integer productId) throws ResourceNotFoundException {
        List<BuyingPrice> buyingPrices = buyingPriceRepository.findAllByProduct_ProductId(productId);
        if (buyingPrices.isEmpty()) {
            throw new ResourceNotFoundException("No buying prices found for product with ID: " + productId);
        }
        return buyingPrices.stream().map(buyingPriceMapper::toBuyingPriceDTO).toList();

    }

    @Override
    public BuyingPriceDTO getCurrentBuyingPriceByProductId(Integer productId) throws ResourceNotFoundException {
        BuyingPrice buyingPrice = buyingPriceRepository.findByProduct_ProductIdAndIsCurrent(productId, true);
        if (buyingPrice == null) {
            throw new ResourceNotFoundException("No current buying price found for product with ID: " + productId);
        }
        return buyingPriceMapper.toBuyingPriceDTO(buyingPrice);
    }

    @Override
    public BuyingPriceDTO createBuyingPrice(Integer productId, BuyingPriceDTO buyingPriceDTO) {
        BuyingPrice newBuyingPrice = buyingPriceMapper.toBuyingPrice(buyingPriceDTO);
        BuyingPrice currentBuyingPrice = buyingPriceRepository.findByProduct_ProductIdAndIsCurrent(productId, true);

        if (currentBuyingPrice != null) {
            if ((currentBuyingPrice.getBuyingPriceValue().compareTo(newBuyingPrice.getBuyingPriceValue()) == 0) && (currentBuyingPrice.getBuyingPriceFluctuation().compareTo(newBuyingPrice.getBuyingPriceFluctuation()) == 0)) {
                return buyingPriceMapper.toBuyingPriceDTO(currentBuyingPrice);
            }
            currentBuyingPrice.setIsCurrent(false);
            buyingPriceRepository.save(currentBuyingPrice);
        }

        newBuyingPrice.setProduct(Product.builder().productId(productId).build());
        return buyingPriceMapper.toBuyingPriceDTO(buyingPriceRepository.save(newBuyingPrice));
    }

}

