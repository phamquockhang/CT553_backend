package com.pqkhang.ct553_backend.domain.booking.cart.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDetailInfoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartDetailService {
    List<CartDetailDTO> createCartDetail(@Valid CartDetailInfoDTO cartDetailInfoDTO) throws ResourceNotFoundException;

    List<CartDetailDTO> getCartDetailByCartId(Integer cartId) throws ResourceNotFoundException;

    List<CartDetailDTO> updateCartDetail(@Valid CartDetailDTO cartDetailDTO) throws ResourceNotFoundException;

    void deleteCartDetail(Integer cartDetailId) throws ResourceNotFoundException;
}
