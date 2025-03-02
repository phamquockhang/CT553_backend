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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class CartDetailServiceImpl implements CartDetailService {
    CartRepository cartRepository;
    CartDetailMapper cartDetailMapper;
    CartDetailRepository cartDetailRepository;

    @Override
    @Transactional
    public List<CartDetailDTO> createCartDetail(CartDetailInfoDTO cartDetailInfoDTO) throws ResourceNotFoundException {
        int cartId = cartDetailInfoDTO.getCartId();
        cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng với id: " + cartId));

        List<CartDetailDTO> cartDetailDTOList = cartDetailInfoDTO.getCartDetails();
        for (CartDetailDTO cartDetailDTO : cartDetailDTOList) {
            int productId = cartDetailDTO.getProductId();
            CartDetail existedCartDetail = cartDetailRepository.findByCart_CartIdAndProduct_ProductId(cartId, productId);
            if (existedCartDetail != null) {
                existedCartDetail.setQuantity(existedCartDetail.getQuantity() + cartDetailDTO.getQuantity());
                cartDetailRepository.save(existedCartDetail);
                continue;
            }

            CartDetail newCartDetail = cartDetailMapper.toCartDetail(cartDetailDTO);
            newCartDetail.setCart(Cart.builder().cartId(cartId).build());
            newCartDetail.setProduct(Product.builder().productId(productId).build());
            cartDetailRepository.save(newCartDetail);
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

    @Override
    @Transactional
    public List<CartDetailDTO> updateCartDetail(CartDetailDTO cartDetailDTO) throws ResourceNotFoundException {
        int cartDetailId = cartDetailDTO.getCartDetailId();
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi tiết giỏ hàng với id: " + cartDetailId));

        if (cartDetailDTO.getQuantity() == 0) {
//            cartDetailRepository.deleteById(cartDetailId);
            cartDetailRepository.customDeleteCartDetailById(cartDetailId);

            return getCartDetailByCartId(cartDetail.getCart().getCartId());
        }

        cartDetail.setQuantity(cartDetailDTO.getQuantity());
        cartDetailRepository.save(cartDetail);
        return getCartDetailByCartId(cartDetail.getCart().getCartId());
    }

    @Override
    @Transactional
    public void deleteCartDetail(Integer cartDetailId) throws ResourceNotFoundException {
        cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi tiết giỏ hàng với id: " + cartDetailId));

        try {
            cartDetailRepository.customDeleteCartDetailById(cartDetailId);
//            cartDetailRepository.delete(cartDetail);
//            cartDetailRepository.flush(); // Đảm bảo Hibernate thực hiện xóa ngay lập tức

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.toString());
        }
    }
}

