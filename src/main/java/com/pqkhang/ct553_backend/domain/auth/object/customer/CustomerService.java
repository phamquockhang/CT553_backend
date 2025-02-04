package com.pqkhang.ct553_backend.domain.auth.object.customer;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.request.ChangePasswordRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface CustomerService {
    Page<CustomerDTO> getCustomers(Map<String, String> params) throws ResourceNotFoundException;

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(UUID id) throws ResourceNotFoundException;

    CustomerDTO createCustomer(CustomerDTO customerDTO) throws ResourceNotFoundException;

    CustomerDTO getLoggedInCustomer();

    CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO) throws ResourceNotFoundException;

    void deleteCustomer(UUID id) throws ResourceNotFoundException;

    void changePassword(UUID id, ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException;

    UUID getCustomerIdByEmail(String email);
}
