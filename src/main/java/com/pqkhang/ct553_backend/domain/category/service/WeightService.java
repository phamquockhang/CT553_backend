package com.pqkhang.ct553_backend.domain.category.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.category.dto.WeightDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeightService {
    List<WeightDTO> getAllWeightsByProductId(Integer productId) throws ResourceNotFoundException;

    WeightDTO getCurrentWeightByProductId(Integer productId) throws ResourceNotFoundException;

    WeightDTO createWeight(Integer productId, @Valid WeightDTO weightDTO);
}
