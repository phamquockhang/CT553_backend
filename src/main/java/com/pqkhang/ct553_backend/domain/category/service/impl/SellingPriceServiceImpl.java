package com.pqkhang.ct553_backend.domain.category.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.category.dto.SellingPriceDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.entity.SellingPrice;
import com.pqkhang.ct553_backend.domain.category.mapper.SellingPriceMapper;
import com.pqkhang.ct553_backend.domain.category.repository.SellingPriceRepository;
import com.pqkhang.ct553_backend.domain.category.service.SellingPriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class SellingPriceServiceImpl implements SellingPriceService {

    SellingPriceRepository sellingPriceRepository;
    SellingPriceMapper sellingPriceMapper;

    @Override
    public List<SellingPriceDTO> getAllSellingPricesByProductId(Integer productId) throws ResourceNotFoundException {
        List<SellingPrice> sellingPrices = sellingPriceRepository.findAllByProduct_ProductId(productId);
        if (sellingPrices.isEmpty()) {
            throw new ResourceNotFoundException("No selling prices found for product with ID: " + productId);
        }
        return sellingPrices.stream().map(sellingPriceMapper::toSellingPriceDTO).toList();

    }

    @Override
    public SellingPriceDTO getCurrentSellingPriceByProductId(Integer productId) throws ResourceNotFoundException {
        SellingPrice sellingPrice = sellingPriceRepository.findByProduct_ProductIdAndIsCurrent(productId, true);
        if (sellingPrice == null) {
            throw new ResourceNotFoundException("No current selling price found for product with ID: " + productId);
        }
        return sellingPriceMapper.toSellingPriceDTO(sellingPrice);
    }

    @Override
    public SellingPriceDTO createSellingPrice(Integer productId, SellingPriceDTO sellingPriceDTO) {
        SellingPrice newSellingPrice = sellingPriceMapper.toSellingPrice(sellingPriceDTO);
        SellingPrice currentSellingPrice = sellingPriceRepository.findByProduct_ProductIdAndIsCurrent(productId, true);

        if (currentSellingPrice != null) {
            if ((currentSellingPrice.getSellingPriceValue().compareTo(newSellingPrice.getSellingPriceValue()) == 0) && (currentSellingPrice.getSellingPriceFluctuation().compareTo(newSellingPrice.getSellingPriceFluctuation()) == 0)) {
                return sellingPriceMapper.toSellingPriceDTO(currentSellingPrice);
            }
            currentSellingPrice.setIsCurrent(false);
            sellingPriceRepository.save(currentSellingPrice);
        }

        newSellingPrice.setProduct(Product.builder().productId(productId).build());
        return sellingPriceMapper.toSellingPriceDTO(sellingPriceRepository.save(newSellingPrice));
    }

}

