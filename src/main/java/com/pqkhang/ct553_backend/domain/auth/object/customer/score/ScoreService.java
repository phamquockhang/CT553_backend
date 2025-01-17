package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ScoreService {
    List<ScoreDTO> getAllScoresByCustomerId(UUID customerId) throws ResourceNotFoundException;

//    Page<ScoreDTO> getCustomers(Map<String, String> params) throws ResourceNotFoundException;
//    List<ScoreDTO> getAllCustomers();
//    ScoreDTO getCustomerById(UUID id) throws ResourceNotFoundException;
//    ScoreDTO createCustomer(ScoreDTO scoreDTO) throws ResourceNotFoundException;
//    ScoreDTO getLoggedInCustomer();
//    ScoreDTO updateCustomer(UUID id, ScoreDTO scoreDTO) throws ResourceNotFoundException;
//    void deleteCustomer(UUID id) throws ResourceNotFoundException;
//    void changePassword(UUID id, ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException;
}
