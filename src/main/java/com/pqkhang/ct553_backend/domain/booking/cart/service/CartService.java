package com.pqkhang.ct553_backend.domain.booking.cart.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CartService {
    CartDTO createCartByCustomerId(UUID customerId) throws ResourceNotFoundException;

    CartDTO getCartByCustomerId(UUID customerId) throws ResourceNotFoundException;
}
