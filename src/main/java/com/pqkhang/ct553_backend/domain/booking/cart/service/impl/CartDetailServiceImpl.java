package com.pqkhang.ct553_backend.domain.booking.cart.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDetailInfoDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.entity.Cart;
import com.pqkhang.ct553_backend.domain.booking.cart.entity.CartDetail;
import com.pqkhang.ct553_backend.domain.booking.cart.mapper.CartDetailMapper;
import com.pqkhang.ct553_backend.domain.booking.cart.repository.CartDetailRepository;
import com.pqkhang.ct553_backend.domain.booking.cart.repository.CartRepository;
import com.pqkhang.ct553_backend.domain.booking.cart.service.CartDetailService;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class CartDetailServiceImpl implements CartDetailService {
    CartRepository cartRepository;
    CartDetailMapper cartDetailMapper;
    CartDetailRepository cartDetailRepository;
    ProductRepository productRepository;

    @Override
    public List<CartDetailDTO> createCartDetail(CartDetailInfoDTO cartDetailInfoDTO) throws ResourceNotFoundException {
        int cartId = cartDetailInfoDTO.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng với id: " + cartId));

        List<CartDetailDTO> cartDetailDTOList = cartDetailInfoDTO.getCartDetails();
        System.out.println("------------------------------" + cartDetailDTOList);
        for (CartDetailDTO cartDetailDTO : cartDetailDTOList) {
            CartDetail cartDetail = cartDetailMapper.toCartDetail(cartDetailDTO);
            cartDetail.setCart(Cart.builder().cartId(cartId).build());

//            cartDetail.setProduct(Product.builder().productId(cartDetailDTO.getProductId()).build());
            Product product = productRepository.findById(cartDetailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + cartDetailDTO.getProductId()));
            cartDetail.setProduct(product);

//            cart.getCartDetails().add(cartDetail);
            cartDetailRepository.save(cartDetail);
        }
        List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_CartId(cartId);
        return cartDetails.stream()
                .map(cartDetailMapper::toCartDetailDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartDetailDTO> getCartDetailByCartId(Integer cartId) throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng với id: " + cartId));

        return cart.getCartDetails().stream()
                .map(cartDetailMapper::toCartDetailDTO)
                .collect(Collectors.toList());
    }
}

