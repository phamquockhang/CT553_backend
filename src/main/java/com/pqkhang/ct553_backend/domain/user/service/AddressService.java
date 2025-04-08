package com.pqkhang.ct553_backend.domain.user.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.user.dto.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface AddressService {
    List<AddressDTO> getAllAddressesByCustomerId(UUID customerId) throws ResourceNotFoundException;

    AddressDTO getDefaultAddressByCustomerId(UUID customerId) throws ResourceNotFoundException;

    void setDefaultAddress(UUID customerId, UUID addressId) throws ResourceNotFoundException;

    AddressDTO createAddress(UUID customerId, AddressDTO addressDTO) throws ResourceNotFoundException;

    AddressDTO updateAddress(UUID id, AddressDTO addressDTO) throws ResourceNotFoundException;

    void deleteAddress(UUID id) throws ResourceNotFoundException;
}
