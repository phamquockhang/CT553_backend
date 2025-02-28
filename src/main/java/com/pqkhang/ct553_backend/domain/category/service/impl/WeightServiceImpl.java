package com.pqkhang.ct553_backend.domain.category.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.category.dto.WeightDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.entity.Weight;
import com.pqkhang.ct553_backend.domain.category.mapper.WeightMapper;
import com.pqkhang.ct553_backend.domain.category.repository.WeightRepository;
import com.pqkhang.ct553_backend.domain.category.service.WeightService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class WeightServiceImpl implements WeightService {

    WeightRepository weightRepository;
    WeightMapper weightMapper;

    @Override
    public List<WeightDTO> getAllWeightsByProductId(Integer productId) throws ResourceNotFoundException {
        List<Weight> weights = weightRepository.findAllByProduct_ProductId(productId);
        if (weights.isEmpty()) {
            throw new ResourceNotFoundException("No weight found for product with ID: " + productId);
        }
        return weights.stream().map(weightMapper::toWeightDTO).toList();
    }

    @Override
    public WeightDTO getCurrentWeightByProductId(Integer productId) throws ResourceNotFoundException {
        Weight weight = weightRepository.findByProduct_ProductIdAndIsCurrent(productId, true);
        if (weight == null) {
            throw new ResourceNotFoundException("No current weight found for product with ID: " + productId);
        }
        return weightMapper.toWeightDTO(weight);
    }

    @Override
    public WeightDTO createWeight(Integer productId, WeightDTO weightDTO) {
        Weight newWeight = weightMapper.toWeight(weightDTO);
        Weight currentWeight = weightRepository.findByProduct_ProductIdAndIsCurrent(productId, true);

        if (currentWeight != null) {
            if (currentWeight.getWeightValue().compareTo(newWeight.getWeightValue()) == 0) {
                return weightMapper.toWeightDTO(currentWeight);
            }
            currentWeight.setIsCurrent(false);
            weightRepository.save(currentWeight);
        }

        newWeight.setProduct(Product.builder().productId(productId).build());
        return weightMapper.toWeightDTO(weightRepository.save(newWeight));
    }

}

