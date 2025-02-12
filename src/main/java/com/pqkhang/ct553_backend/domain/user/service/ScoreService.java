package com.pqkhang.ct553_backend.domain.user.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.user.dto.ScoreDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ScoreService {
    List<ScoreDTO> getAllScoresByCustomerId(UUID customerId) throws ResourceNotFoundException;

    ScoreDTO getCurrentScoreByCustomerId(UUID customerId) throws ResourceNotFoundException;

    ScoreDTO createScore(UUID customerId, @Valid ScoreDTO scoreDTO);
}
