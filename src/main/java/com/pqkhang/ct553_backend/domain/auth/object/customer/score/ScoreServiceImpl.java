package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.object.customer.Customer;
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

    @Override
    public ScoreDTO getCurrentScoreByCustomerId(UUID customerId) throws ResourceNotFoundException {
        Score score = scoreRepository.findByCustomer_CustomerIdAndIsCurrent(customerId, true);
        if (score == null) {
            throw new ResourceNotFoundException("No current score found for customer with ID: " + customerId);
        }
        return scoreMapper.toScoreDTO(score);
    }

    @Override
    public ScoreDTO createScore(UUID customerId, ScoreDTO scoreDTO) {
        Score newScore = scoreMapper.toScore(scoreDTO);
        Score currentScore = scoreRepository.findByCustomer_CustomerIdAndIsCurrent(customerId, true);
        if (currentScore != null) {
            currentScore.setIsCurrent(false);
            newScore.setNewValue(currentScore.getNewValue() + newScore.getChangeAmount());
            scoreRepository.save(currentScore);
        } else {
            newScore.setNewValue(newScore.getChangeAmount());
        }
        newScore.setCustomer(Customer.builder().customerId(customerId).build());
        return scoreMapper.toScoreDTO(scoreRepository.save(newScore));
    }

}

