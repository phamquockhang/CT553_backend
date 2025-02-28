package com.pqkhang.ct553_backend.domain.booking.cart.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.entity.Cart;
import com.pqkhang.ct553_backend.domain.booking.cart.mapper.CartMapper;
import com.pqkhang.ct553_backend.domain.booking.cart.service.CartService;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CustomerRepository customerRepository;
    CartMapper cartMapper;

    @Override
    public CartDTO createCartByCustomerId(UUID customerId, CartDTO cartDTO) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));

        if (customer.getCart() != null) {
            return cartMapper.toCartDTO(customer.getCart());
        } else {
            Cart cart = cartMapper.toCart(cartDTO);
            cart.setCustomer(Customer.builder().customerId(customerId).build());
            customer.setCart(cart);

            return cartMapper.toCartDTO(customerRepository.save(customer).getCart());
        }
    }

    @Override
    public CartDTO getCartByCustomerId(UUID customerId) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));

//        if (customer.getCart() == null) {
//            throw new ResourceNotFoundException("Không tìm thấy giỏ hàng của khách hàng với id: " + customerId);
//        }

        return cartMapper.toCartDTO(customer.getCart());
    }
}

