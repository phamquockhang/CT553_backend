package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class ScoreServiceImpl implements ScoreService {

    ScoreRepository scoreRepository;
    ScoreMapper scoreMapper;

    @Override
    public List<ScoreDTO> getAllScoresByCustomerId(UUID customerId) throws ResourceNotFoundException {
        List<Score> scores = scoreRepository.findAllByCustomer_CustomerId(customerId);
        if (scores.isEmpty()) {
            throw new ResourceNotFoundException("No scores found for customer with ID: " + customerId);
        }
        return scores.stream().map(scoreMapper::toScoreDTO).toList();

    }
}

