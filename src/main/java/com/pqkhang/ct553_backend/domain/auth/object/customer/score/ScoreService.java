package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
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
